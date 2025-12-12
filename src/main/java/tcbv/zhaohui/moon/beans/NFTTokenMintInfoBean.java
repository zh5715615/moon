package tcbv.zhaohui.moon.beans;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author: zhaohui
 * @Title: NFTTokenInfoBean
 * @Description:
 * @date: 2025/12/12 13:36
 */
@Builder
@Getter
@Setter
@ApiModel("铸造卡片响应体")
public class NFTTokenMintInfoBean {
    @ApiModelProperty("nft合约地址")
    private String nftAddress;

    @ApiModelProperty("nft token id")
    private String tokenId;

    @ApiModelProperty("mint交易hash")
    private String txHash;
}
