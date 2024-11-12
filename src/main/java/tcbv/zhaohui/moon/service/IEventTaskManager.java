package tcbv.zhaohui.moon.service;

import java.time.LocalDateTime;

public interface IEventTaskManager {
    /**
     * 添加任务
     * @param taskName 任务名称
     * @param task 任务
     * @param dateTime 任务执行事件
     */
    void addTask(String taskName, Runnable task, LocalDateTime dateTime);

    /**
     * 取消任务
     * @param taskName 任务名称
     */
    void cancelTask(String taskName);

    /**
     * 关闭任务池
     */
    void shutdown();
}
