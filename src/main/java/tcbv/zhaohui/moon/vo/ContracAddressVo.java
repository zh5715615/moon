package tcbv.zhaohui.moon.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

/**
 * @author: zhaohui
 * @Title: ContracAddressVo
 * @Description:
 * @date: 2025/12/19 15:01
 */
@Builder
@Getter
@ApiModel("系统-合约地址信息")
public class ContracAddressVo {
    @ApiModelProperty("dapp池地址")
    private String dappPoolAddress;

    @ApiModelProperty("卡片NFT地址")
    private String cardNFTAddress;

    @ApiModelProperty("usdt地址")
    private String usdtTokenAddress;

    @ApiModelProperty("spaceJediToken地址")
    private String spaceJediTokenAddress;
}
