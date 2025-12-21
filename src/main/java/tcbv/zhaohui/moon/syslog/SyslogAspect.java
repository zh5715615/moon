package tcbv.zhaohui.moon.syslog;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import tcbv.zhaohui.moon.entity.SyslogEntity;
import tcbv.zhaohui.moon.utils.GsonUtil;
import tcbv.zhaohui.moon.jwt.JwtUtil;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author: zhaohui
 * @Title: SysLogAspect
 * @Description:
 * @date: 2025/12/19 11:29
 */
@Aspect
@Component
@Slf4j
public class SyslogAspect {

    @Autowired
    private SyslogCallback syslogCallback;

    @Pointcut("@annotation(tcbv.zhaohui.moon.syslog.Syslog)")
    public void logPointCut() {

    }

    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) {

    }

    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        Integer httpStatus = null;
        String errorMsg = "";
        long startTime = System.currentTimeMillis();

        try {
            result = joinPoint.proceed(); // 执行原方法

            RequestAttributes ra = RequestContextHolder.getRequestAttributes();
            ServletRequestAttributes sra = (ServletRequestAttributes) ra;
            HttpServletResponse response = sra.getResponse();
            httpStatus = response.getStatus(); // 获取 HTTP 状态码

        } catch (Throwable e) {
            errorMsg = e.getMessage();
            httpStatus = 500; // 设置默认错误状态码
            log.error("接口执行出错: {}", e.getMessage());
            throw e; // 重新抛出异常，保证业务逻辑中断
        } finally {
            saveLog(joinPoint, startTime, result, httpStatus, errorMsg);
        }

        return result;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long startTime, Object result, Integer httpStatus, String errorMsg) {
        Object responseMsg = result == null ? errorMsg : result;

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        String userAgentInfo = request.getHeader("User-Agent");

        String address = "";
        String auth = request.getHeader("Authorization");
        String token = null;

        if (auth != null && auth.startsWith("Bearer ")) {
            token = auth.substring("Bearer ".length()).trim();
        }
        if (token != null && !token.isEmpty()) {
            try {
                address = JwtUtil.requireUserId(token);
            } catch (Exception e) {
                log.error("token解析失败", e.getMessage());
            }
        }
        String requestPath = request.getRequestURI();
        String fromIp = getClientIp(request);
        String uri = request.getRequestURI();
        String method = request.getMethod();

        // TODO: 异步线程池
        String finalAddress = address;
        CompletableFuture.runAsync(() -> {
            try {
                SyslogEntity syslogEntity = buildLogInfo(joinPoint);
                syslogEntity.setAddress(finalAddress);
                syslogEntity.setAgent(userAgentInfo);
                syslogEntity.setApiInterface(requestPath);
                syslogEntity.setHttpStatus(httpStatus);
                syslogEntity.setIp(fromIp);

                long useTime = System.currentTimeMillis() - startTime;
                syslogEntity.setDurationMs(useTime);
                syslogEntity.setCreateTime(new Date());

                if (httpStatus == 200) {
                    Rsp rsp = (Rsp) responseMsg;
                    syslogEntity.setErrorCode(rsp.getCode());
                    syslogEntity.setErrorMsg(rsp.getMessage());
                    Object data = rsp.getData();
                    if (data instanceof Object) {
                        syslogEntity.setResponseParam(GsonUtil.toJson(rsp.getData(), true));
                    } else {
                        syslogEntity.setResponseParam(data.toString());
                    }

                }

                log.info("Request:{}, {}, {}, params:{}", syslogEntity.getOperation(), method, uri, syslogEntity.getRequestParam());

                // handle_log 保存到数据库或消息队列
                syslogCallback.handler(syslogEntity);

            } catch (Exception e) {
                log.error("日志记录失败", e);
            }
        });
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = null;
        String UNKNOWN = "unknown";

        // 1. X-Forwarded-For: Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");
        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // 2. Proxy-Client-IP: Apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // 3. WL-Proxy-Client-IP: WebLogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // 4. HTTP_CLIENT_IP: 某些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || UNKNOWN.equalsIgnoreCase(ipAddresses)) {
            // 5. X-Real-IP: Nginx 服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        // 多个代理时，取第一个 IP（通常是客户端真实 IP）
        if (ipAddresses != null && ipAddresses.length() > 0) {
            // 多个 IP 用逗号分隔，取第一个
            ip = ipAddresses.split(",")[0].trim();
        }

        // 如果仍无法获取，则使用远程地址（可能是网关或反向代理）
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    private SyslogEntity buildLogInfo(ProceedingJoinPoint joinPoint) {
        SyslogEntity syslogEntity = new SyslogEntity();

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        String module = ms.getMethod().getAnnotation(Syslog.class).module();
        syslogEntity.setModule(module);

        handleApiOperation(joinPoint, syslogEntity);

        Map<String, Object> pathVariablesMap = new HashMap<>();
        Map<String, Object> requestParamMap = new HashMap<>();
        String requestBody = "";

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        String[] parameterNames = signature.getParameterNames();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Object argValue = joinPoint.getArgs()[i];

            // 跳过文件上传、响应对象等非普通参数
            if (argValue instanceof MultipartFile ||
                    argValue instanceof MultipartFile[] ||
                    argValue instanceof ServletResponse ||
                    argValue instanceof ServletRequest) {
                continue;
            }

            boolean isPathVariable = false;
            boolean isRequestBody = false;

            for (Annotation annotation : parameterAnnotations[i]) {
                if (annotation.annotationType().equals(PathVariable.class)) {
                    isPathVariable = true;
                }
                if (annotation.annotationType().equals(RequestBody.class)) {
                    isRequestBody = true;
                }
            }

            if (isPathVariable) {
                pathVariablesMap.put(parameterNames[i], argValue);
            } else if (isRequestBody) {
                requestBody = JSON.toJSONString(argValue);
            } else {
                requestParamMap.put(parameterNames[i], argValue);
            }
        }

        ImmutableMap.Builder<String, Object> unionParamMapBuilder = new ImmutableMap.Builder<>();

        if (!pathVariablesMap.isEmpty()) {
            unionParamMapBuilder.put("path-variables", pathVariablesMap);
        }

        if (!requestParamMap.isEmpty()) {
            unionParamMapBuilder.put("request-params", requestParamMap);
        }

        Map<String, Object> unionParamMap;
        if (StringUtils.isBlank(requestBody)) {
            unionParamMap = unionParamMapBuilder.build();
        } else {
            unionParamMap = unionParamMapBuilder.put("body-object", JSON.parse(requestBody)).build();
        }

        String requestParams = JSON.toJSONString(unionParamMap);
        syslogEntity.setRequestParam(requestParams);
        return syslogEntity;
    }

    public void handleApiOperation(JoinPoint joinPoint, SyslogEntity syslogEntity) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);

        String actionMethod;
        if (apiOperation != null) {
            actionMethod = apiOperation.value();
        } else {
            actionMethod = method.getName();
        }

        syslogEntity.setOperation(actionMethod);
    }
}
