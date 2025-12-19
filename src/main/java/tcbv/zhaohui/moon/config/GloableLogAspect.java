package tcbv.zhaohui.moon.config;

import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson.JSON;
import tcbv.zhaohui.moon.utils.GsonUtil;

/**
 * @author: zhaohui
 * @Title: GloableLogAspect
 * @Description:
 * @date: 2025/12/19 10:34
 */
@Aspect
@Component
@Slf4j
public class GloableLogAspect {
    @Around("execution(* tcbv.zhaohui.moon..*.*(..))")
    public Object logAnnotatedMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        // 对入参进行捕获打印
        if (log.isDebugEnabled()) {
            if (args.length != 0) {
                // 有参打印方式
                int i = 0;
                for (Object arg : args) {
                    try {
                        log.debug("Entering annotated method: {}.{}. Args[{}]: {}", className, methodName, i, JSON.toJSONString(arg, SerializerFeature.IgnoreNonFieldGetter));
                    } catch (Exception e) {
                        log.debug("不能解析{}-{}方法入参：", className, methodName);
                    }
                    i++;
                }
            } else {
                // 无参打印方式
                log.info("Entering annotated method: {}.{}, ", className, methodName);
            }
        }

        Object result;
        try {
            result = joinPoint.proceed(); // 执行原方法
            if (log.isDebugEnabled()) {
                if (result != null) {
                    // 有出参打印方式
                    parsingException(className, methodName, result);
                } else {
                    // 无出参打印方式
                    log.debug("Exiting annotated method: {}.{}", className, methodName);
                }
            }
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                // 拦截异常退出打印方式
                log.debug("Exception in annotated method {}.{}: {}", className, methodName, e.getMessage());
            }
            throw e; // 重新抛出异常，保证原逻辑
        }

        return result;
    }

    /**
     * 处理返回值的打印
     */
    private void parsingException(String className, String methodName, Object result) {
        try {
            log.debug("Exiting annotated method: {}.{}. Return: {}", className, methodName, GsonUtil.toJson(result));
        } catch (Exception e) {
            log.debug("不能解析{}-{}方法出参：", className, methodName);
        }
    }
}
