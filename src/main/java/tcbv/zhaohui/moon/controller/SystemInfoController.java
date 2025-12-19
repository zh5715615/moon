package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.ContracAddressVo;

/**
 * @author: zhaohui
 * @Title: SystemInfoController
 * @Description:
 * @date: 2025/12/19 15:00
 */
@RestController
@RequestMapping("/api/v1/system")
@Validated
@ResponseBody
@Api(tags="系统信息管理")
@Slf4j
public class SystemInfoController {

    @Autowired
    private Web3Config web3Config;

    @ApiOperation("获取合约地址信息")
    @GetMapping("/contract/info")
    public Rsp<ContracAddressVo> contractInfo() {
        ContracAddressVo vo = ContracAddressVo.builder()
                .usdtTokenAddress(web3Config.getUsdtContractAddress())
                .spaceJediTokenAddress(web3Config.getSpaceJediContractAddress())
                .cardNFTAddress(web3Config.getCardNftContractAddress())
                .dappPoolAddress(web3Config.getDappPoolContractAddress())
                .build();
        return Rsp.okData(vo);
    }
}
