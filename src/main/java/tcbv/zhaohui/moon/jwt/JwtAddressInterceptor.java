package tcbv.zhaohui.moon.jwt;

/**
 * @author: zhaohui
 * @Title: JwtAddressInterceptor
 * @Description:
 * @date: 2025/12/21 9:44
 */
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAddressInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod hm = (HandlerMethod) handler;

        // 先看方法上有没有注解，没有就看类上有没有
        JwtAddressRequired ann = hm.getMethodAnnotation(JwtAddressRequired.class);
        if (ann == null) {
            ann = hm.getBeanType().getAnnotation(JwtAddressRequired.class);
        }
        if (ann == null) {
            return true; // 没贴注解：不处理
        }

        String token = extractBearerToken(request);
        if (token == null) {
            if (ann.required()) {
                write401(response, "Missing Authorization Bearer token");
                return false;
            }
            return true;
        }

        try {
            Claims claims = JwtUtil.verifyAndGetClaims(token); // ✅ 你现有的验签解析
            String userId = claims.get(ann.userId(), String.class);

            if (StringUtils.isBlank(userId) && ann.required()) {
                write401(response, "Missing userId: " + ann.userId());
                return false;
            }

            // 方式1：放到 ThreadLocal（推荐：Controller 里随处 JwtContext.getAddress()）
            JwtContext.setUserId(userId);

            // 方式2：也放到 request attribute（可选）
            request.setAttribute("user_id", userId);

            String address = claims.get(ann.address(), String.class);
            if (StringUtils.isBlank(address) && ann.required()) {
                write401(response, "Missing address: " + ann.address());
                return false;
            }
            JwtContext.setAddress(address);
            request.setAttribute("address", address);

            String expectedRole = ann.role();
            if (!expectedRole.isEmpty()) {
                String actualRole = claims.get("role", String.class); // 假设 JWT 中角色字段叫 "role"
                if (!expectedRole.equals(actualRole)) {
                    write401(response, "Insufficient role: expected '" + expectedRole + "', but got '" + actualRole + "'");
                    return false;
                }
            }
            return true;

        } catch (ExpiredJwtException e) {
            write401(response, "Token expired");
            return false;
        } catch (JwtException e) {
            write401(response, "Invalid token");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex) {
        // ✅ 一定要清理，避免线程复用导致串号
        JwtContext.clear();
    }

    private String extractBearerToken(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (auth == null) return null;
        if (!auth.startsWith("Bearer ")) return null;
        String token = auth.substring("Bearer ".length()).trim();
        return token.isEmpty() ? null : token;
    }

    private void write401(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\"}");
    }
}
