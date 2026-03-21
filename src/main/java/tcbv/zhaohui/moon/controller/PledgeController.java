package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.dto.WithdrawDto;
import tcbv.zhaohui.moon.entity.ChainTxTaskEntity;
import tcbv.zhaohui.moon.entity.PledgeEntity;
import tcbv.zhaohui.moon.enums.ChainTxBizType;
import tcbv.zhaohui.moon.enums.PledgeRegion;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.ChainTxTaskService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.PledgeService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.tasks.chain.ChainTaskParams;
import tcbv.zhaohui.moon.tasks.chain.ChainTxTaskDispatcher;
import tcbv.zhaohui.moon.utils.EnumUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.TaskStatusVo;
import tcbv.zhaohui.moon.vo.PledgeHistoryVo;
import tcbv.zhaohui.moon.vo.PledgeRegionVo;
import tcbv.zhaohui.moon.vo.PromoteHistoryVo;
import tcbv.zhaohui.moon.vo.UserPledgeInfoVo;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private ChainTxTaskService chainTxTaskService;

    @Autowired
    private ChainTxTaskDispatcher chainTxTaskDispatcher;

    @Syslog(module = "PLEDGE")
    @PostMapping("/invoke")
    @ApiOperation("质押")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> invoke(@RequestBody @Valid TransactionDto dto) {
        ChainTaskParams params = new ChainTaskParams();
        params.setTxHash(dto.getTxHash());
        params.setUserId(JwtContext.getUserId());
        params.setAddress(JwtContext.getAddress());
        ChainTxTaskEntity task = new ChainTxTaskEntity();
        task.setTxHash(dto.getTxHash());
        task.setBizType(ChainTxBizType.PLEDGE_INVOKE.name());
        task.setBizParams(GsonUtil.toJson(params, false));
        chainTxTaskService.insert(task);
        chainTxTaskDispatcher.dispatch(task);
        return Rsp.okData(TaskStatusVo.of(task.getId(), 0, null));
    }

    @Syslog(module = "PLEDGE")
    @PostMapping("/withdraw")
    @ApiOperation("提取质押奖励")
    @JwtAddressRequired
    public Rsp<TaskStatusVo> withdraw(@RequestBody @Valid WithdrawDto dto) {
        ChainTaskParams params = new ChainTaskParams();
        params.setTxHash(dto.getTxHash());
        params.setUserId(JwtContext.getUserId());
        params.setAddress(JwtContext.getAddress());
        params.setPledgeId(dto.getPledgeId());
        ChainTxTaskEntity task = new ChainTxTaskEntity();
        task.setTxHash(dto.getTxHash());
        task.setBizType(ChainTxBizType.PLEDGE_WITHDRAW.name());
        task.setBizParams(GsonUtil.toJson(params, false));
        chainTxTaskService.insert(task);
        chainTxTaskDispatcher.dispatch(task);
        return Rsp.okData(TaskStatusVo.of(task.getId(), 0, null));
    }

    @GetMapping("/region")
    @ApiOperation("获取质押区域")
    public Rsp<List<PledgeRegionVo>> region() {
        List<PledgeRegionVo> pledgeRegionVoList = new ArrayList<>();
        for (PledgeRegion pledgeRegion : PledgeRegion.values()) {
            PledgeRegionVo pledgeRegionVo = new PledgeRegionVo();
            pledgeRegionVo.setRegionCode(pledgeRegion);
            pledgeRegionVo.setPledgeAmount(pledgeRegion.getAmount());
            BigDecimal rewardPercent = dappPoolService.getCurrentRewardPercent(pledgeRegion);
            if (web3Config.isEnvProd()) {
                pledgeRegionVo.setPledgePeriod(pledgeRegion.getPeriodProd());
                pledgeRegionVo.setPledgeRevenue(rewardPercent.multiply(BigDecimal.valueOf(pledgeRegion.getPeriodProd()).divide(BigDecimal.valueOf(30 * 24 * 3600L), 2, RoundingMode.HALF_UP)).doubleValue());
            } else {
                pledgeRegionVo.setPledgePeriod(pledgeRegion.getPeriodTest());
                pledgeRegionVo.setPledgeRevenue(rewardPercent.multiply(BigDecimal.valueOf(pledgeRegion.getPeriodTest()).divide(BigDecimal.valueOf(30 * 60L), 2, RoundingMode.HALF_UP)).doubleValue());
            }
            pledgeRegionVoList.add(pledgeRegionVo);
        }
        return Rsp.okData(pledgeRegionVoList);
    }

    @GetMapping("/userInfo")
    @ApiOperation("用户质押信息")
    @JwtAddressRequired
    public Rsp<List<UserPledgeInfoVo>> getUserPledge() {
        String userId = JwtContext.getUserId();
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "create_time"));
        PledgeEntity queryEntity = new PledgeEntity();
        queryEntity.setUserId(userId);
        Page<PledgeEntity> pageEntity = pledgeService.queryByPage(queryEntity, pageRequest);
        if (pageEntity == null || pageEntity.getContent().isEmpty()) {
            return Rsp.okData(Collections.emptyList());
        }
        List<UserPledgeInfoVo> userPledgeInfoVoList = new ArrayList<>();
        for (PledgeEntity pledgeEntity : pageEntity.getContent()) {
            UserPledgeInfoVo userPledgeInfoVo = new UserPledgeInfoVo();
            PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", pledgeEntity.getRegion());
            if (pledgeRegion == null) {
                log.warn("pledge region is null, pledgeId:{}", pledgeEntity.getId());
                continue;
            }
            int period = web3Config.isEnvProd() ? pledgeRegion.getPeriodProd() : pledgeRegion.getPeriodTest();
            userPledgeInfoVo.setRegionCode(pledgeRegion.name());
            userPledgeInfoVo.setExpirationTime(pledgeEntity.getExpireTime());
            BigDecimal percent = dappPoolService.getCurrentRewardPercent(pledgeRegion);
            BigDecimal reward = percent.multiply(BigDecimal.valueOf(pledgeEntity.getAmount()));
            int baseCycle = web3Config.isEnvProd() ? 30 * 24 * 3600 : 30 * 60;
            BigDecimal pledgeRevenueAmount = reward.multiply(BigDecimal.valueOf(period)).divide(BigDecimal.valueOf(baseCycle), 2, RoundingMode.HALF_UP);
            userPledgeInfoVo.setPledgeRevenueAmount(pledgeRevenueAmount.doubleValue());
            userPledgeInfoVo.setPledgeAmount(pledgeEntity.getAmount());
            userPledgeInfoVo.setPledgeDatetime(pledgeEntity.getCreateTime());
            userPledgeInfoVo.setPledgePeriod(period);
            userPledgeInfoVo.setPledgeRevenuePercent(percent.multiply(BigDecimal.valueOf(period)).divide(BigDecimal.valueOf(baseCycle), 2, RoundingMode.HALF_UP).doubleValue());
            long nowTimestamp = System.currentTimeMillis();
            long diff = pledgeEntity.getExpireTime().getTime() >= nowTimestamp ? pledgeEntity.getExpireTime().getTime() - nowTimestamp : 0L;
            BigDecimal progress = BigDecimal.ONE.subtract(BigDecimal.valueOf(diff).divide(BigDecimal.valueOf(period * 1000L), 2, RoundingMode.HALF_UP));
            userPledgeInfoVo.setProgress(progress.doubleValue());
            userPledgeInfoVo.setPledgeId(pledgeEntity.getId());
            userPledgeInfoVoList.add(userPledgeInfoVo);
        }
        return Rsp.okData(userPledgeInfoVoList);
    }

    @GetMapping("/history")
    @ApiOperation("全站动态（质押列表）")
    public Rsp<List<PledgeHistoryVo>> history() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "create_time"));
        Page<PledgeEntity> pageEntity = pledgeService.queryByPage(new PledgeEntity(), pageRequest);
        if (pageEntity == null || pageEntity.getContent().isEmpty()) {
            return Rsp.okData(Collections.emptyList());
        }
        List<PledgeHistoryVo> pledgeHistoryVoList = new ArrayList<>();
        for (PledgeEntity pledgeEntity : pageEntity.getContent()) {
            PledgeHistoryVo pledgeHistoryVo = new PledgeHistoryVo();
            PledgeRegion pledgeRegion = EnumUtil.fromFieldValue(PledgeRegion.class, "level", pledgeEntity.getRegion());
            int period = web3Config.isEnvProd() ? pledgeRegion.getPeriodProd() : pledgeRegion.getPeriodTest();
            int baseCycle = web3Config.isEnvProd() ? 30 * 24 * 3600 : 30 * 60;
            pledgeHistoryVo.setHash(pledgeEntity.getPledgeHash());
            pledgeHistoryVo.setAddress(pledgeEntity.getAddress());
            pledgeHistoryVo.setPledgeDatetime(pledgeEntity.getCreateTime());
            if (pledgeEntity.getWithdrawAmount() != null) {
                BigDecimal preAmount = BigDecimal.valueOf(pledgeEntity.getAmount());
                BigDecimal afterAmount = BigDecimal.valueOf(pledgeEntity.getWithdrawAmount());
                pledgeHistoryVo.setPledgeRevenue(afterAmount.subtract(preAmount).divide(preAmount, 2, RoundingMode.HALF_UP).doubleValue());
            } else {
                BigDecimal percent = dappPoolService.getCurrentRewardPercent(pledgeRegion);
                pledgeHistoryVo.setPledgeRevenue(percent.multiply(BigDecimal.valueOf(period)).divide(BigDecimal.valueOf(baseCycle), 2, RoundingMode.HALF_UP).doubleValue());
            }
            pledgeHistoryVo.setPledgeAmount(pledgeEntity.getAmount());
            pledgeHistoryVo.setPledgePeriod(period);
            pledgeHistoryVoList.add(pledgeHistoryVo);
        }
        return Rsp.okData(pledgeHistoryVoList);
    }
}
