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

    PUBLIC_READ_WRITE("2"),

    AUTHENTICATED_READ("3"),

    LOG_DELIVERY_WRITE("4"),

    BUCKET_OWNER_READ("5"),

    BUCKET_OWNER_FULL_CONTROL("6"),

    AWS_EXEC_READ("7");

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
        if (this == PRIVATE) {
            return PolicyType.READ_WRITE;
        }
        if (this == PUBLIC_READ) {
            return PolicyType.READ;
        }

        return PolicyType.READ_WRITE;
    }

    public CannedAccessControlList getAcl() {
        if (this == PRIVATE) {
            return CannedAccessControlList.Private;
        }
        if (this == PUBLIC_READ) {
            return CannedAccessControlList.PublicRead;
        }
        if (this == PUBLIC_READ_WRITE) {
            return CannedAccessControlList.PublicReadWrite;
        }

        return CannedAccessControlList.PublicReadWrite;
    }

}