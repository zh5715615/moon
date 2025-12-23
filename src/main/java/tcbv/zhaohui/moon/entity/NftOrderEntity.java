package tcbv.zhaohui.moon.entity;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 卡片订单(NftOrder)实体类
 *
 * @author makejava
 * @since 2025-12-23 20:58:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NftOrderEntity {
    /**
     * 订单id
     */
    private Integer id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * NFT id
     */
    private String tokenId;
    /**
     * 价格
     */
    private Double price;
    /**
     * 订单状态(1挂单，2取消，3成交)
     */
    private Integer status;
    /**
     * 买方用户id
     */
    private String buyerId;
    /**
     * 成交hash
     */
    private String tradeHash;
    /**
     * 下单时间
     */
    private Date createTime;
    /**
     * 成交时间
     */
    private Date tradeTime;
    /**
     * 提交交易hash值
     */
    private String submitHash;
    /**
     * 取消交易hash值
     */
    private String cancelHash;
    /**
     * 取消订单时间
     */
    private Date cancelTime;
    /**
     * 用户地址
     */
    private String address;
}

