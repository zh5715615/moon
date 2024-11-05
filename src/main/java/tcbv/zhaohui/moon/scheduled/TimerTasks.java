//package tcbv.zhaohui.moon.scheduled;
//
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
///**
// * @author dawn
// * @date 2024/11/2 15:13
// */
//@Component
//public class TimerScheduled {
//    // 程序加载后只会调用一次
//    @Scheduled(fixedDelay  = 2000)
//    public void initializeTimers() throws InterruptedException {
//        LocalDateTime now = LocalDateTime.now();
//        long secondsToNextTenMinutes = calculateSecondsToNextMultiple(now, 10);
//        long secondsToNextFiveMinutes = calculateSecondsToNextMultiple(now, 5);
//
//        long secondsToNextTick = Math.min(secondsToNextTenMinutes, secondsToNextFiveMinutes);
//
//        // 等待直到下一个时间点
//        Thread.sleep(secondsToNextTick * 1000L);
//
//        if (secondsToNextTick == secondsToNextTenMinutes) {
//           // triggerTaskForTenMinutes(secondsToNextTenMinutes);
//        }
//        if (secondsToNextTick == secondsToNextFiveMinutes) {
//            //triggerTaskForFiveMinutes(secondsToNextFiveMinutes);
//        }
//
//    }
//
//    private static long calculateSecondsToNextMultiple(LocalDateTime now, int multiple) {
//        int currentMinute = now.getMinute();
//        int nextMultiple = ((currentMinute / multiple) + 1) * multiple;
//        int minutesToAdd = nextMultiple - currentMinute;
//        return minutesToAdd * 60;
//    }
//
////    private static void triggerTaskForTenMinutes(long time) {
////        TimerMaps.setRemainingTime("gameTwo", time);
////        System.out.println("触发十分钟定时任务：" + LocalDateTime.now());
////    }
////
////    private static void triggerTaskForFiveMinutes(long time) {
////        TimerMaps.setRemainingTime("gameTwo", time);
////        System.out.println("触发五分钟定时任务：" + LocalDateTime.now());
////    }
//
//}
