package tcbv.zhaohui.moon.oss;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
/**
 * @author: zhaohui
 * @Title: OssConfig
 * @Description:
 * @date: 2025/12/2 20:58
 */

@Data
@Component
@ConfigurationProperties(prefix = "star-wars.oss")
public class OssConfig {
    private String accessKey;

    private String secretKey;

    private String domain;

    private String walletBucketName;

    private String nftBucketName;

    private String endpoint;

    private String region;

    private String isHttps;

    private String accessPolicy;
}