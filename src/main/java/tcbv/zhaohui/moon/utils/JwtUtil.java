package tcbv.zhaohui.moon.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

/**
 * @author: zhaohui
 * @Title: JwtUtil
 * @Description:
 * @date: 2025/12/19 16:23
 */
public class JwtUtil {
    private static final SecretKey KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public static String generateToken(String userId, long ttlSeconds, Map<String, Object> extraClaims) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(ttlSeconds);

        return Jwts.builder()
                .subject(userId)                       // sub
                .issuedAt(Date.from(now))              // iat
                .expiration(Date.from(exp))            // exp
                .claims(extraClaims)                   // 自定义字段（可选）
                .signWith(KEY)                         // HS256
                .compact();
    }

    public static Jws<Claims> verify(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
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
}
