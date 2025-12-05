package tcbv.zhaohui.moon.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author: zhaohui
 * @Title: AesUtil
 * @Description:
 * @date: 2025/12/3 8:34
 */
public class AesUtil {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

    // 固定 16 字节 IV（示例用常量）
    private static final String IV = "f9@Qz7#L1p$X2vK8";  // 恰好16个字符

    /**
     * 加密
     * @param key       16 字节密钥字符串（如 "1234567890abcdef"）
     * @param plainText 明文
     * @return Base64 编码的密文
     */
    public static String encrypt(String key, String plainText) throws Exception {
        SecretKeySpec secretKey = buildKey(key);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密
     * @param key        16 字节密钥字符串（必须和加密时一致）
     * @param cipherText Base64 编码的密文
     * @return 解密后的明文
     */
    public static String decrypt(String key, String cipherText) throws Exception {
        SecretKeySpec secretKey = buildKey(key);
        IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes(StandardCharsets.UTF_8));

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    /**
     * 直接使用 16 字节 key 构造 SecretKeySpec
     */
    private static SecretKeySpec buildKey(String key) {
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length != 16) {
            throw new IllegalArgumentException("AES key 必须是 16 字节，当前是 " + keyBytes.length + " 字节");
        }
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}
