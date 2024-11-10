package tcbv.zhaohui.moon.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author dawn
 * @date 2024/11/2 15:13
 */
//@Component
public class TimerScheduled {

    @Scheduled(fixedDelay = 1000)
    public void initializeTimers() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime gameOwo = TimerMaps.getRemainingTime(TimerMaps.GAMEONE);
        LocalDateTime gameTwo = TimerMaps.getRemainingTime(TimerMaps.GAMETWO);
        if (gameOwo == null || gameTwo == null) {
            // 获取当前时间的分钟部分
            int currentMinute = now.getMinute();
            // 计算到下一个十的整数倍还需要多少分钟
            int minutesToNextTen = (10 - currentMinute % 10) % 10;
            // 在当前时间上加上这个分钟数
            LocalDateTime nextTen = now.plusMinutes(minutesToNextTen);
            if (gameOwo == null) {
                TimerMaps.setRemainingTime(TimerMaps.GAMEONE, nextTen);
            }
            if (gameTwo == null) {
                TimerMaps.setRemainingTime(TimerMaps.GAMETWO, nextTen);
            }
            return;
        }
        // 计算两个时间之间的间隔
        Duration oneTime = Duration.between(gameOwo, now);
        Duration twoTime = Duration.between(gameTwo, now);
        // 获取间隔的绝对值，以秒为单位
        long oneTimes = Math.abs(oneTime.getSeconds());
        long twoTimes = Math.abs(twoTime.getSeconds());
        if (oneTimes <= 0) {
            // 十分钟后的时间
            LocalDateTime tenMinutesLater = now.plusMinutes(10);
            TimerMaps.setRemainingTime(TimerMaps.GAMEONE, tenMinutesLater);
        }
        if (twoTimes <= 0) {
            // 十分钟后的时间
            LocalDateTime tenMinutesLater = now.plusMinutes(5);
            TimerMaps.setRemainingTime(TimerMaps.GAMETWO, tenMinutesLater);
        }

    }

}
