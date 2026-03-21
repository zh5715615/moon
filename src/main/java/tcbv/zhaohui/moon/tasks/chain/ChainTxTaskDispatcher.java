package tcbv.zhaohui.moon.tasks.chain;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.service.ChainTxTaskService;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.util.List;

@Component
@Slf4j
public class ChainTxTaskDispatcher {

    @Autowired
    @Qualifier("chainTxExecutor")
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private ChainTxTaskService chainTxTaskService;

    @Autowired
    private ChainTxWorkerFactory workerFactory;

    @Value("${star-wars.chain-task.max-retry:3}")
    private int maxRetry;

    @Value("${star-wars.chain-task.retry-delay-seconds:10}")
    private long retryDelaySeconds;

    /** 提交一个任务到线程池 */
    public void dispatch(ChainTxTaskEntity task) {
        executor.submit(() -> process(task));
    }

    /** 应用启动后恢复 PENDING / PROCESSING 的存量任务 */
    @EventListener(ApplicationReadyEvent.class)
    public void recoverOnStartup() {
        List<ChainTxTaskEntity> pending = chainTxTaskService.queryPending();
        if (!pending.isEmpty()) {
            log.info("[ChainTxTask] 启动恢复任务数量: {}", pending.size());
            pending.forEach(this::dispatch);
        }
    }

    private void process(ChainTxTaskEntity task) {
        log.info("[ChainTxTask] 开始处理 id={} bizType={} txHash={}", task.getId(), task.getBizType(), task.getTxHash());
        try {
            chainTxTaskService.updateStatus(task.getId(), 1); // PROCESSING
            ChainTaskParams params = GsonUtil.fromJson(task.getBizParams(), ChainTaskParams.class);
            ChainTxWorker worker = workerFactory.getWorker(task.getBizType());
            worker.execute(task, params);
            chainTxTaskService.deleteById(task.getId());
            log.info("[ChainTxTask] 处理成功 id={}", task.getId());
        } catch (Exception e) {
            log.error("[ChainTxTask] 处理失败 id={} error={}", task.getId(), e.getMessage(), e);
            int newRetry = task.getRetryCount() + 1;
            if (newRetry >= maxRetry) {
                chainTxTaskService.updateFailed(task.getId(), e.getMessage());
                log.error("[ChainTxTask] 超过最大重试次数，标记失败 id={}", task.getId());
            } else {
                chainTxTaskService.updateRetry(task.getId(), newRetry);
                // 延迟后重新提交
                executor.submit(() -> {
                    try {
                        Thread.sleep(retryDelaySeconds * 1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                    task.setRetryCount(newRetry);
                    process(task);
                });
            }
        }
    }
}
