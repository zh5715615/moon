package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author dawn  游戏开奖
 * @date 2024/11/5 10:45
 */
@Component
@Slf4j
public class GamePrizeDrawScheduled {
    @Scheduled(fixedRate = 25000)
    public void executeTask() {
        LocalDateTime gameOneTime = TimerMaps.getRemainingTime("gameOne");
        LocalDateTime gameTwoTime = TimerMaps.getRemainingTime("gameTwo");
        if(gameOneTime==null&&gameTwoTime==null){
            log.error("开奖游戏剩余时间获取失败");
        }
        // 获取当前时间
        LocalDateTime now = LocalDateTime.now();

        // 计算两个时间之间的间隔
        Duration oneTime = Duration.between(gameOneTime, now);
        Duration twoTime = Duration.between(gameTwoTime, now);
        // 获取间隔的绝对值，以秒为单位
        long oneTimes = Math.abs(oneTime.getSeconds());
        long twoTimes = Math.abs(twoTime.getSeconds());
        if(oneTimes<60){
            //判断时间是否已经开奖

        }
        if(twoTimes<60){
            //判断时间是否已经开奖

        }

    }
}
