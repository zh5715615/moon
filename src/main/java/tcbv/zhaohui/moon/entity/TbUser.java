package tcbv.zhaohui.moon.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2024/11/2 14:48
 */@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TbUser  implements Serializable {
    /** 用户id */
    private String id ;
    /** 用户地址 */
    private String address ;
    /** 用户名 */
    private String userName ;
    /** 创建时间 */
    private Date createTime ;
}
