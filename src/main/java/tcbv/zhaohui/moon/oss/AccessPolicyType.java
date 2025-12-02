package tcbv.zhaohui.moon.oss;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: zhaohui
 * @Title: AccessPolicyType
 * @Description:
 * @date: 2025/12/2 21:01
 */
@Getter
@AllArgsConstructor
public enum AccessPolicyType {

    PRIVATE("0"),
    PUBLIC_READ("1"),
    PublicReadWrite("2"),
    AuthenticatedRead("3"),
    LogDeliveryWrite("4"),
    BucketOwnerRead("5"),
    BucketOwnerFullControl("6"),
    AwsExecRead("7");

    private final String accessPolicy;

    private static final Map<String, AccessPolicyType> MAP = new HashMap<>();


    public static AccessPolicyType getByType(String accessPolicy) {
        return MAP.get(accessPolicy);
    }

    static {
        for (AccessPolicyType accessPolicyType : AccessPolicyType.values()) {
            MAP.put(accessPolicyType.getAccessPolicy(), accessPolicyType);
        }
    }


    public PolicyType getPolicyType() {
        // TODO
        if (this == PRIVATE) {
            return PolicyType.READ_WRITE;
        }
        if (this == PUBLIC_READ) {
            return PolicyType.READ;
        }

        return PolicyType.READ_WRITE;
    }

    public CannedAccessControlList getAcl() {
        // TODO
        if (this == PRIVATE) {
            return CannedAccessControlList.Private;
        }
        if (this == PUBLIC_READ) {
            return CannedAccessControlList.PublicRead;
        }
        if (this == PublicReadWrite) {
            return CannedAccessControlList.PublicReadWrite;
        }

        return CannedAccessControlList.PublicReadWrite;
    }

}