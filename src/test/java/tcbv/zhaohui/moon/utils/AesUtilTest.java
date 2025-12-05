package tcbv.zhaohui.moon.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author: zhaohui
 * @Title: AesUtilTest
 * @Description:
 * @date: 2025/12/3 8:38
 */
@Slf4j
class AesUtilTest {
    @Test
    void test() throws Exception {
        String key = "abcd9876ABWE!@#$";
        String data = "HelloWorld";
        String cipherText = AesUtil.encrypt(key, data);
        String plainText = AesUtil.decrypt(key, cipherText);
        log.info("plainText: {}", plainText);
        Assertions.assertEquals(data, plainText);
    }
}
