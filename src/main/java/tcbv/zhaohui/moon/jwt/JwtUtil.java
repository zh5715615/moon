package tcbv.zhaohui.moon.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

/**
 * @author: zhaohui
 * @Title: JwtUtil
 * @Description:
 * @date: 2025/12/19 16:23
 */
@Component
public class JwtUtil {
    private static SecretKey key;

    @Value("${star-wars.jwt.secret-base64}")
    private String secretBase64;

    @PostConstruct
    public void init() {
        if (secretBase64 == null || secretBase64.trim().isEmpty()) {
            throw new IllegalStateException("jwt.secret-base64 must be configured!");
        }
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretBase64));
    }

    public static String generateToken(String userId, long ttlSeconds, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);

        return Jwts.builder()
                .subject(userId)                       // sub
                .issuedAt(Date.from(now))              // iat
                .expiration(Date.from(exp))            // exp
                .claims(extraClaims)                   // 自定义字段（可选）
                .signWith(key)                         // HS256
                .compact();
    }

    public static Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token);
    }

    public static String requireUserId(String token) {
        try {
            Claims claims = verify(token).getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("TOKEN_EXPIRED");
        } catch (JwtException e) {
            // 包含签名错误、格式错误、篡改等
            throw new RuntimeException("TOKEN_INVALID");
        }
    }

    public static Claims verifyAndGetClaims(String token) {
        return verify(token).getPayload();
    }
}
