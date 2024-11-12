package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tcbv.zhaohui.moon.service.IEventTaskManager;

import java.time.LocalDateTime;
import java.util.concurrent.*;

@Slf4j
@Service
public class EventTaskManager implements IEventTaskManager {

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    private final ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Override
    public void addTask(String taskName, Runnable task, LocalDateTime dateTime) {
        long delay = calculateDelay(dateTime);
        if (delay < 0) {
            log.info("Task {} is scheduled in the past, ignoring.", taskName);
            return;
        }

        ScheduledFuture<?> scheduledFuture = scheduler.schedule(task, delay, TimeUnit.MILLISECONDS);
        scheduledTasks.put(taskName, scheduledFuture);
        log.info("Task {} added successfully. It will run at ", dateTime);
    }

    @Override
    public void cancelTask(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.remove(taskName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            log.info("Task {} canceled successfully.", taskName);
        } else {
            log.info("Task {} not found.", taskName);
        }
    }

    @Override
    public void shutdown() {
        scheduler.shutdown();
        log.info("Scheduler shut down.");
    }

    private long calculateDelay(LocalDateTime dateTime) {
        LocalDateTime now = LocalDateTime.now();
        if (dateTime.isBefore(now)) {
            return -1; // 如果指定时间点在当前时间之前，返回负数
        }
        return java.time.Duration.between(now, dateTime).toMillis();
    }
}
