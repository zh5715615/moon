package tcbv.zhaohui.moon.oss;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: BucketType
 * @Description:
 * @date: 2025/12/12 11:33
 */
@AllArgsConstructor
@Getter
public enum BucketType {
    PRIVATE_BUCKET("star-wars",false),

    PUBLIC_BUCKET("card-nft", true);

    private final String bucketName;

    private final boolean pub;
}
