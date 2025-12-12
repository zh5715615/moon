package tcbv.zhaohui.moon.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (NFTAttributesBean)
 *
 * @author zhaohui
 * @since 2025/12/12 9:30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NFTAttributesBean {
    //特征类型
    private String traitType;
    //特征值
    private String value;
}