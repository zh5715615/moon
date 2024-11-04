package tcbv.zhaohui.moon.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * (FileUtilsTest) json与对象转换工具类
 *
 * @author zhaohui
 * @since 2023/4/4 16:50
 */
public class GsonUtil {
    private GsonUtil() {}

    private static final Gson GSON = new GsonBuilder()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> DateUtil.parse(json.getAsString()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfSrc, context) -> new JsonPrimitive(DateUtil.format(date)))
            .disableHtmlEscaping().setPrettyPrinting().create();

    private static final Gson GSON_COMPRESS = new GsonBuilder()
            .serializeSpecialFloatingPointValues()
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> DateUtil.parse(json.getAsString()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, typeOfSrc, context) -> new JsonPrimitive(DateUtil.format(date)))
            .disableHtmlEscaping().create();

    public static String toJson(Object object, boolean compress){
        return compress ? GSON_COMPRESS.toJson(object) : GSON.toJson(object);
    }

    public static String toJson(Object object){
        return toJson(object, false);
    }

    public static <T> T fromJson(String json, Class<T> clz){
        return GSON.fromJson(json, clz);
    }

    public static <T> T fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    public static class DateUtil{
        private DateUtil() {}

        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        static String format(Date date){
            if (date == null) {
                return null;
            }
            return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).format(formatter);
        }

        static Date parse(String dateStr){
            Instant instant = LocalDateTime.parse(dateStr, formatter).atZone(ZoneId.systemDefault()).toInstant();
            return Date.from(instant);
        }
    }

}
