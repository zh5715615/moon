package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DiceRollerSchedule {
    @Scheduled(cron = "0 0/10 * * * ? ")
    public void startScheduleOn() {
        log.info("摇骰子投注时间，可以下注.");
    }

    @Scheduled(cron = "0 9/10 * * * ? ")
    public void startScheduleOff() {
        log.info("摇骰子开奖时间，不能下注.");
    }
}
