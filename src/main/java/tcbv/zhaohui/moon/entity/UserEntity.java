package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户表(User)实体类
 *
 * @author makejava
 * @since 2025-12-19 17:14:43
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private String id;

    private String address;

    private String token;

    private Date createTime;

    private String parentId;

    private Integer promoCode;
}

