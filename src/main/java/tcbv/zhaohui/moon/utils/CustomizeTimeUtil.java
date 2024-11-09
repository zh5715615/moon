package tcbv.zhaohui.moon.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class CustomizeTimeUtil {

    /**
     * 根据给定的时间计算最近的intervalInMinutes分钟时间段的开始时间和结束时间的时间戳。
     *
     * @param currentTime 给定的时间
     * @return 包含开始时间和结束时间的时间戳的 long 数组
     */
    public static Pair<Long, Long> getFiveMinuteRange(LocalDateTime currentTime, int intervalInMinutes) {
        // 获取给定时间的 LocalTime 部分
        LocalTime now = currentTime.toLocalTime();

        // 计算最近的5分钟时间段的开始时间和结束时间
        LocalTime startTime = now.truncatedTo(ChronoUnit.MINUTES).minusMinutes(now.getMinute() % intervalInMinutes);
        LocalTime endTime = startTime.plusMinutes(intervalInMinutes);

        // 将LocalTime转换为Instant以获取时间戳
        Instant startTimestamp = currentTime.toLocalDate().atTime(startTime).atZone(ZoneId.systemDefault()).toInstant();
        Instant endTimestamp = currentTime.toLocalDate().atTime(endTime).atZone(ZoneId.systemDefault()).toInstant();

        // 返回时间戳
        return Pair.of(startTimestamp.toEpochMilli(), endTimestamp.toEpochMilli());
    }

    /**
     * 将毫秒值转换为格式化的日期字符串。
     *
     * @param timestamp 毫秒值
     * @return 格式化的日期字符串
     */
    public static String formatTimestamp(long timestamp) {
        // 将毫秒值转换为 Instant
        Instant instant = Instant.ofEpochMilli(timestamp);

        // 将 Instant 转换为 LocalDateTime
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化日期
        return localDateTime.format(formatter);
    }

    public static long localDateTime2Long(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return zonedDateTime.toInstant().toEpochMilli();
    }
}
