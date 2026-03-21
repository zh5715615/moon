# Moon 服务端设计文档

## 1. 项目概述

Moon（应用名 `star-wars`）是一个基于 BSC（币安智能链）的区块链游戏平台后端服务，提供钱包登录、NFT 卡牌交易、质押理财、斗牛链上游戏等功能。

- **技术栈**：Spring Boot 2.5.0 / Java 17 / Maven
- **数据库**：MySQL + MyBatis + Druid 连接池
- **链交互**：Web3j（BSC，chainId=97 测试网）
- **存储**：MinIO（OSS）
- **接口文档**：Swagger（Knife4j）
- **部署**：HTTPS 8443 端口，Jenkins CI/CD，jar 包部署至 `47.92.224.70`

---

## 2. 架构总览

```
┌─────────────────────────────────────────────────────┐
│                    Client / DApp                    │
└────────────────────────┬────────────────────────────┘
                         │ HTTPS :8443
┌────────────────────────▼────────────────────────────┐
│              Spring Boot (star-wars)                │
│                                                     │
│  Controller ──► Service ──► DAO (MyBatis) ──► MySQL │
│       │             │                               │
│       │        chain/Service ──► Web3j ──► BSC 链   │
│       │             │                               │
│  JWT 拦截器     OSS (MinIO)                         │
│  CORS Filter    @Scheduled 定时任务                  │
│  @Syslog AOP    GloableLogAspect                    │
└─────────────────────────────────────────────────────┘
```

---

## 3. 模块说明

### 3.1 认证（JWT）

- 登录方式：**钱包签名登录**（`/api/v1/moon/login/walletLogin`）和**管理员账密登录**（`/api/v1/moon/login/admin/login`）。
- 钱包登录通过 `Web3CryptoUtil` 验证以太坊签名，确认地址所有权后签发 JWT。
- JWT 携带 `userId`（sub）和 `address`（自定义 claim），由 `JwtUtil`（HMAC-SHA256）生成。
- `JwtAddressInterceptor` 拦截全部请求，遇到 `@JwtAddressRequired` 注解的接口则强制校验 `Authorization` Header。
- 当前请求的用户信息通过 `JwtContext`（ThreadLocal）在业务层随取随用。

### 3.2 用户与推广体系

- 用户表 `tb_user`：以钱包地址（`address`）为唯一标识，`parent_id` 记录直接上级，`promo_code` 为推广码。
- 推广奖励：质押成功后按 `star-wars.promote-reward-percentage`（默认 10%）向上级发放推广奖励，记录在 `tb_pledge_promotion`。

### 3.3 质押（Pledge）

流程：
1. 前端在链上调用 DappPool 合约的质押方法，获得 `txHash`。
2. 调用 `/api/v1/moon/pledge/invoke`，后端通过 `DappPoolService.parsePledgeEvent` 解析链上事件，校验交易合法性。
3. 写入 `tb_pledge`，同步发放推广奖励（若有上级）。

提取：同理通过链上 `txHash` 解析 Withdraw 事件后写库。

质押区间（`PledgeRegion` 枚举）决定锁定周期和收益率，收益率从合约 `DappPoolService.getCurrentRewardPercent` 实时读取。

### 3.4 NFT 卡牌

- 铸造（Mint）：上传图片至 MinIO → 生成元数据 JSON 上传 → 调用链上 `CardNFTTokenService.mint`，写入 `user_nft_current_holding`。
- 挂单/交易/取消：前端链上操作后提交 `txHash`，后端解析 `DappPool` 合约事件（`SubmitOrderEvent`/`NFTTradeOrderEvent`/`CancelOrderEvent`）验证后写入 `tb_nft_order`。
- NFT 订单状态枚举：`NftOrderStatusEnum`（PENDING / TRADED / CANCEL）。

### 3.5 斗牛游戏（Bullfighting）

**游戏规则**：标准牛牛，5 人每人 5 张牌，取 3 张之和为 10 的倍数，剩余 2 张点数之和个位数即为牛值（0=无牛，10=牛牛）。

**发牌合规保证**（`PokerDealer.dealHands`）：
- 标准 5×5 局自动校验：至少一手有牛，且有牛的手中最大牛值唯一（无并列冠军），最多重试 100 次。

**完整流程**：
1. 用户购买游戏次数：链上转账 → 提交 `txHash` → 后端验证 → 写 `tb_bullfighting_purchase`，触发 `BuyGameTimesEvent`。
2. 开始游戏（`/start`）：后端发牌，计算各手牛值，记录对局至 `tb_bullfighting_record`，累计积分至 `tb_bullfighting_score`。
3. 排行榜（`/ranking`）：按当日积分倒序，顺序排名（非密集排名）。
4. 奖励发放（`SendRewardTask`，每日定时）：
   - 取前一日积分排行，按名次按比例分配奖池。
   - 调用链上 `BullfightingGameSampleService.reward` 批量发放，写入 `tb_bullfighting_reward`。
   - 幂等保护：同一 `gameDate` 只发一次。

奖池余额通过 `BullfightingGameSampleService.getPoolBalance` 从链上实时查询。

### 3.6 链上服务层

| 服务接口 | 对应合约 | 职责 |
|---|---|---|
| `EthereumService` | — | Web3j 连接、账户凭证、ETH 转账、交易校验 |
| `Token20Service` | USDT (BEP-20) | 代币余额、转账 |
| `DappPoolService` | DappPool | 质押/提取/NFT 挂单事件解析、收益率查询 |
| `CardNFTTokenService` | CardNFT (ERC-721) | Mint、持仓查询 |
| `BullfightingGameSampleService` | Game | 奖池余额、批量发奖 |

合约地址通过 `web3.contract.*` 配置项注入，每个环境独立配置。

### 3.7 系统日志

`@Syslog(module = "...")` 注解配合 `SyslogAspect` AOP，对重要接口的入参、出参和耗时自动记录至 `tb_sys_log`。`GloableLogAspect` 则对全局异常进行日志捕获。

### 3.8 定时任务

| 任务类 | 触发时机 | 职责 |
|---|---|---|
| `SendRewardTask` | 每日定时（cron） | 计算前日斗牛排行，批量链上发奖 |
| `CreateLiquidityPoolTask` | 定时 | 流动性池相关操作 |

---

## 4. API 路由总览

| 前缀 | Controller | 主要功能 |
|---|---|---|
| `/api/v1/moon/login` | LoginController | 钱包登录、管理员登录 |
| `/api/v1/moon/bullfighting` | BullfightingController | 购买次数、开始游戏、排行榜、查询奖池 |
| `/api/v1/moon/pledge` | PledgeController | 质押、提取、历史记录 |
| `/api/v1/moon/card` | CardNftController | NFT 铸造、挂单、交易、取消 |
| `/api/v1/moon/promote` | PromoteController | 推广信息、奖励历史 |
| `/api/v1/moon/sysinfo` | SystemInfoController | 系统参数查询 |
| `/api/v1/moon/account` | AccountMgrController | 账户管理（管理端） |
| `/api/v1/moon/sjpkg` | SJPackageTxController | SJ 套餐交易 |

所有接口均需跨域支持，由 `MoonCorsFilter` 处理。

---

## 5. 数据库表说明

| 表名 | 说明 |
|---|---|
| `tb_user` | 用户（钱包地址、推广关系） |
| `tb_wallet` | 钱包余额快照 |
| `tb_pledge` | 质押记录 |
| `tb_pledge_promotion` | 推广奖励记录 |
| `tb_nft_order` | NFT 交易订单 |
| `user_nft_current_holding` | 用户当前 NFT 持仓 |
| `tb_bullfighting_purchase` | 斗牛游戏购买次数记录 |
| `tb_bullfighting_record` | 斗牛对局记录 |
| `tb_bullfighting_score` | 斗牛积分 |
| `tb_bullfighting_reward` | 斗牛奖励发放记录 |
| `tb_sj_package_tx` | SJ 套餐交易记录 |
| `tb_sys_log` | 系统操作日志 |

---

## 6. 部署

CI/CD 由 Jenkins 驱动（`docs/moon-svc.jenkins`）：
1. 从 GitHub 拉取指定分支代码。
2. `mvn clean package -Dmaven.test.skip=true` 打包。
3. SCP 传输 jar 至服务器 `/root/prj/moon/svc`，执行 `run.sh` 重启服务。

环境配置通过 Spring Profile 区分（`dev` / `tst` / `pro`），启动时指定 `--spring.profiles.active=xxx`。
