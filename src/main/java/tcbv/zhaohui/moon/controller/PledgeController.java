package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.beans.events.PledgeEventBean;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.beans.events.WithdrawEventBean;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.dto.WithdrawDto;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.PledgeService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.Rsp;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author: zhaohui
 * @Title: PledgeController
 * @Description:
 * @date: 2025/12/21 10:55
 */
@RestController
@RequestMapping("/api/v1/moon/pledge")
@Slf4j
@Api(tags = "登录相关接口")
public class PledgeController {

    @Autowired
    private PledgeService pledgeService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private Web3Config web3Config;

    @Syslog(module = "PLEDGE")
    @PostMapping("/invoke")
    @ApiOperation("质押")
    @JwtAddressRequired
    public Rsp invoke(@RequestBody @Valid TransactionDto dto) throws Exception {
        PledgeEventBean pledgeEventBean = dappPoolService.parsedPledge(dto.getTxHash());
        PledgeRegion pledgeRegion = pledgeEventBean.getRegion();
        Date now = new Date();
        String userId = JwtContext.getUserId();
        String address = JwtContext.getAddress();
        PledgeEntity pledgeEntity = new PledgeEntity();
        pledgeEntity.setUserId(userId);
        pledgeEntity.setAddress(address);
        pledgeEntity.setRegion(pledgeRegion.getLevel());
        pledgeEntity.setAmount(pledgeEventBean.getPledgeAmount());
        int expire = web3Config.isEnvProd() ? pledgeRegion.getPeriodProd() : pledgeRegion.getPeriodTest();
        Date expireTime = DateUtils.addSeconds(now, expire);
        pledgeEntity.setExpireTime(expireTime);
        pledgeEntity.setCreateTime(now);
        pledgeEntity.setPledgeHash(dto.getTxHash());
        pledgeService.insert(pledgeEntity);
        return Rsp.ok();
    }

    @Syslog(module = "PLEDGE")
    @PostMapping("/withdraw")
    @ApiOperation("提取质押奖励")
    @JwtAddressRequired
    public Rsp withdraw(@RequestBody @Valid WithdrawDto dto) throws Exception {
        WithdrawEventBean withdrawEventBean = dappPoolService.parsedWithdraw(dto.getTxHash());
        PledgeRegion pledgeRegion = withdrawEventBean.getRegion();
        String userId = JwtContext.getUserId();
        String address = JwtContext.getAddress();

        PledgeEntity pledgeEntity = new PledgeEntity();
        pledgeEntity.setUserId(userId);
        pledgeEntity.setAddress(address);
        pledgeEntity.setRegion(pledgeRegion.getLevel());
        pledgeEntity.setWithdrawAmount(withdrawEventBean.getWithrawAmount());
        pledgeEntity.setId(dto.getPledgeId());
        pledgeEntity.setWithdrawHash(dto.getTxHash());
        pledgeService.withdraw(pledgeEntity);
        return Rsp.ok();
    }
}
