package tcbv.zhaohui.moon.beans;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * (NFTAttributeBean)nft的metadata数据
 *
 * @author zhaohui
 * @since 2025/12/12 9:30
 */
@Data
@NoArgsConstructor
public class NFTMetadataBean {
    //描述
    private String description;
    //图片链接
    private String image;
    //名称
    private String name;
    //特征列表
    private List<NFTAttributesBean> attributes;

    public NFTMetadataBean(String image) {
        this.image = image;
    }
}