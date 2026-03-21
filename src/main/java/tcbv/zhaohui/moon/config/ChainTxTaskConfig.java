package tcbv.zhaohui.moon.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ChainTxTaskConfig {

    @Value("${star-wars.chain-task.core-pool-size:4}")
    private int corePoolSize;

    @Value("${star-wars.chain-task.max-pool-size:8}")
    private int maxPoolSize;

    @Value("${star-wars.chain-task.queue-capacity:200}")
    private int queueCapacity;

    @Value("${star-wars.chain-task.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    @Bean(name = "chainTxExecutor")
    public ThreadPoolTaskExecutor chainTxExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("chain-tx-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
