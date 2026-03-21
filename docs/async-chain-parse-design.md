 # 链上交易异步解析改造设计文档

## 1. 背景与目标

当前多个接口在接收前端提交的 `txHash` 后，会**同步**调用链上 RPC（`checkTransaction` + `parseXxxEvent`）来验证交易并解析事件数据，再写入数据库。由于 BSC RPC 响应不稳定，这些接口耗时长，严重影响用户体验。

**改造目标**：
- 接口立即返回「已受理」，不等待链上解析完成。
- 后台线程池异步消费待处理任务，完成解析入库。
- 前端可通过轮询任务状态接口感知处理结果。

---

## 2. 需要改造的接口清单

| 接口 | HTTP Method | 涉及的链调用 |
|---|---|---|
| `POST /api/v1/moon/pledge/invoke` | 质押 | `parsePledgeEvent` + `checkTransaction` |
| `POST /api/v1/moon/pledge/withdraw` | 提取质押 | `parseWithdrawEvent` + `checkTransaction` |
| `POST /api/v1/moon/card/submitOrder` | NFT 挂单 | `parseSubmitOrder` + `checkTransaction` |
| `POST /api/v1/moon/card/tradeOrder` | NFT 交易 | `parseTradeOrder` + `checkTransaction` |
| `POST /api/v1/moon/card/cancelOrder` | NFT 取消订单 | `parseCancelOrder` + `checkTransaction` |
| `POST /api/v1/moon/sj-package/buySpaceJediPackage` | 购买 SJ 套餐 | `parseBuySpaceJediPackageEvent` + `checkTransaction` |
| `POST /api/v1/moon/bullfighting/buyGameTimes` | 购买斗牛次数 | `parseBuyGameTimesEvent` + `checkTransaction` |

---

## 3. 整体方案

```
前端
  │
  │  POST /xxx  { txHash, ...bizParams }
  ▼
Controller
  │  1. 校验基本参数（地址归属、幂等检查）
  │  2. 写入 tb_chain_tx_task（状态=PENDING）
  │  3. 立即返回 { taskId, status: "PENDING" }
  ▼
ChainTxTaskDispatcher（ApplicationEvent 或直接 submit）
  │  4. 将任务提交到线程池队列
  ▼
ChainTxWorker（Runnable）
  │  5. 更新状态 → PROCESSING
  │  6. 调用 RPC：checkTransaction + parseXxxEvent
  │  7a. 成功：执行业务写库，更新状态 → SUCCESS，删除或标记记录
  │  7b. 失败：retry_count++，若超限则状态 → FAILED，记录 error_msg
  ▼
前端轮询
  GET /api/v1/moon/task/status?taskId=xxx
  ← { taskId, status, errorMsg }
```

---

## 4. 数据库表设计

### `tb_chain_tx_task`

```sql
CREATE TABLE tb_chain_tx_task (
    id           VARCHAR(36)   NOT NULL COMMENT '任务ID（UUID）',
    tx_hash      VARCHAR(66)   NOT NULL COMMENT '链上交易 Hash',
    biz_type     VARCHAR(32)   NOT NULL COMMENT '业务类型，见 BizType 枚举',
    biz_params   TEXT          NOT NULL COMMENT '业务参数快照（JSON），含 userId、address 等上下文',
    status       TINYINT       NOT NULL DEFAULT 0 COMMENT '0=PENDING 1=PROCESSING 2=SUCCESS 3=FAILED',
    retry_count  TINYINT       NOT NULL DEFAULT 0 COMMENT '已重试次数',
    error_msg    VARCHAR(512)  DEFAULT NULL COMMENT '失败原因',
    create_time  DATETIME      NOT NULL COMMENT '创建时间',
    update_time  DATETIME      NOT NULL COMMENT '最后更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_tx_hash_biz (tx_hash, biz_type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='链上交易异步解析任务缓存表';
```

**字段说明**：

| 字段 | 说明 |
|---|---|
| `id` | 任务唯一标识，返回给前端用于轮询 |
| `tx_hash` | 待解析的链上交易哈希 |
| `biz_type` | 枚举值，决定使用哪个 Worker 处理（见下方枚举） |
| `biz_params` | 接口入参 + JWT 上下文的 JSON 快照，Worker 执行时无需 HTTP 上下文 |
| `status` | 任务状态流转：PENDING → PROCESSING → SUCCESS / FAILED |
| `retry_count` | 已重试次数，达上限（默认 3）后置 FAILED |
| `error_msg` | 异常信息，用于排查 |
| `uk_tx_hash_biz` | 防止同一笔交易同类型重复提交 |

**BizType 枚举**：

| 枚举值 | 对应接口 |
|---|---|
| `PLEDGE_INVOKE` | 质押 |
| `PLEDGE_WITHDRAW` | 提取质押 |
| `NFT_SUBMIT_ORDER` | NFT 挂单 |
| `NFT_TRADE_ORDER` | NFT 交易 |
| `NFT_CANCEL_ORDER` | NFT 取消订单 |
| `SJ_BUY_PACKAGE` | 购买 SJ 套餐 |
| `BULLFIGHTING_BUY_TIMES` | 购买斗牛次数 |

---

## 5. 线程池设计

### 5.1 线程池配置

```yaml
# application.yml 新增
star-wars:
  chain-task:
    core-pool-size: 4       # 核心线程数（链 RPC 为 IO 密集，可适当加大）
    max-pool-size: 8
    queue-capacity: 200     # 内存队列容量
    keep-alive-seconds: 60
    max-retry: 3            # 最大重试次数
    retry-delay-seconds: 10 # 重试间隔（秒）
```

### 5.2 核心组件

```
ChainTxTaskConfig          # @Configuration，声明 ThreadPoolTaskExecutor Bean
ChainTxTaskDispatcher      # @Component，提供 dispatch(ChainTxTaskEntity) 方法，
                           #   submit Runnable 到线程池；服务启动时扫描 PENDING/PROCESSING
                           #   状态的存量任务并重新入队（宕机恢复）
ChainTxWorkerFactory       # 根据 biz_type 路由到对应 Worker
ChainTxWorker<T>           # 接口，实现类有：
  PledgeInvokeWorker
  PledgeWithdrawWorker
  NftSubmitOrderWorker
  NftTradeOrderWorker
  NftCancelOrderWorker
  SjBuyPackageWorker
  BullfightingBuyTimesWorker
```

### 5.3 Worker 执行逻辑（伪代码）

```
void execute(ChainTxTaskEntity task):
    try:
        taskDao.updateStatus(task.id, PROCESSING)
        bizParams = GsonUtil.fromJson(task.bizParams, XxxBizParams.class)

        // 原同步逻辑（checkTransaction + parseXxxEvent + 写库）
        doProcess(bizParams)

        taskDao.delete(task.id)          // 成功后删除记录
    catch Exception e:
        retryCount = task.retryCount + 1
        if retryCount >= maxRetry:
            taskDao.updateFailed(task.id, e.getMessage())
        else:
            taskDao.updateRetry(task.id, retryCount, PENDING)  // 重新置 PENDING 等待重试
            scheduleRetry(task, retryDelaySeconds)             // 延迟重新 submit
```

### 5.4 启动恢复

应用启动时（`@EventListener(ApplicationReadyEvent.class)`），扫描 `tb_chain_tx_task` 中 `status IN (0,1)`（PENDING / PROCESSING）的记录，重新提交到线程池，避免宕机丢任务。

---

## 6. 接口变更说明

### 6.1 改造后的请求/响应

**改造前**：
```json
POST /api/v1/moon/pledge/invoke
← { "code": 0, "msg": "success" }   // 等待链解析完成后返回
```

**改造后**：
```json
POST /api/v1/moon/pledge/invoke
← { "code": 0, "data": { "taskId": "uuid", "status": "PENDING" } }  // 立即返回
```

### 6.2 新增任务状态查询接口

```
GET /api/v1/moon/task/status?taskId={taskId}
@JwtAddressRequired

← {
    "code": 0,
    "data": {
        "taskId": "uuid",
        "status": "SUCCESS" | "PENDING" | "PROCESSING" | "FAILED",
        "errorMsg": "..."   // 仅 FAILED 时有值
    }
}
```

前端建议轮询间隔：2 秒，超时上限：60 秒。

---

## 7. 幂等与安全保障

- `uk_tx_hash_biz` 唯一索引防止重复提交同一笔交易。
- Controller 层保留对 `address` 归属的**前置校验**（不涉及链调用），确保非本人交易早期拒绝。
- Worker 执行业务写库前，仍保留原有的业务幂等检查（如 `existByHash`），防止线程池并发或重试导致重复入库。
- FAILED 记录保留不删除，用于人工排查和告警。

---

## 8. 改造步骤

1. 执行 DDL，创建 `tb_chain_tx_task`。
2. 新增 `ChainTxTaskEntity`、`ChainTxTaskDao`、`ChainTxTaskDao.xml`。
3. 新增 `BizType` 枚举、`ChainTxTaskConfig`（线程池）、`ChainTxTaskDispatcher`、`ChainTxWorkerFactory`。
4. 逐一实现各 `ChainTxWorker`，将原 Controller 中的链调用逻辑迁移进去。
5. 改造 Controller：将链调用替换为「写任务表 + dispatch」，返回 taskId。
6. 新增 `TaskStatusController`