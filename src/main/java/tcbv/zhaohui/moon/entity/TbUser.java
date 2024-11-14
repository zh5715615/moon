package tcbv.zhaohui.moon.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2024/11/2 14:48
 */
@Data
public class TbUser implements Serializable {
    /**
     * 用户id
     */
    private String id;
    /**
     * 用户地址
     */
    private String address;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 密钥
     */
    private String token;
    /**
     * 推广码
     */
    private Integer promoCode;
    /**
     * 上级id
     */
    private String parentId;
}
