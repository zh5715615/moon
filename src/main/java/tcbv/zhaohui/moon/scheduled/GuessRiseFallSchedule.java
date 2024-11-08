package tcbv.zhaohui.moon.scheduled;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GuessRiseFallSchedule {
    @Scheduled(cron = "0 0/5 * * * ? ")
    public void startScheduleOn() {
        log.info("猜BNB涨跌投注时间，可以下注.");
    }

    @Scheduled(cron = "0 4/5 * * * ? ")
    public void startScheduleOff() {
        log.info("猜BNB涨跌投注时间，不能下注.");
    }
}
