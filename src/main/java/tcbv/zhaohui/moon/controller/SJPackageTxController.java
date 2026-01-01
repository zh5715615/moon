package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.beans.PresaleInfoBean;
import tcbv.zhaohui.moon.beans.events.BuySpaceJediPackageEventBean;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import tcbv.zhaohui.moon.enums.PresaleStage;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.SjPackageTxService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.EnumUtil;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.PresaleHistoryVo;
import tcbv.zhaohui.moon.vo.PresaleInfoVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static tcbv.zhaohui.moon.beans.Constants.*;
import static tcbv.zhaohui.moon.enums.PresaleStage.*;
import static tcbv.zhaohui.moon.exceptions.BizException.SYSTEM_ERROR;

/**
 * @author: zhaohui
 * @Title: SJPackageTxController
 * @Description:
 * @date: 2025/12/24 17:05
 */
@RestController
@RequestMapping("/api/v1/moon/sj-package")
@Slf4j
@Api(tags = "sj套餐管理")
public class SJPackageTxController {

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private SjPackageTxService sjPackageTxService;

    @Syslog(module = "SJ-PACKAGE")
    @PostMapping("/buySpaceJediPackage")
    @ApiOperation("购买sj套餐")
    @JwtAddressRequired
    public Rsp buySpaceJediPackage(@RequestBody @Validated TransactionDto dto) throws Exception {
        BuySpaceJediPackageEventBean buySpaceJediPackageEventBean  = dappPoolService.parseBuySpaceJediPackage(dto.getTxHash());
        String userId = JwtContext.getUserId();
        SjPackageTxEntity sjPackageTxEntity = new SjPackageTxEntity();
        sjPackageTxEntity.setBuyerId(userId);
        sjPackageTxEntity.setHash(dto.getTxHash());
        sjPackageTxEntity.setPacakgeCnt(buySpaceJediPackageEventBean.getBuyCnt());
        sjPackageTxEntity.setPrice(buySpaceJediPackageEventBean.getPrice().doubleValue());
        sjPackageTxEntity.setStage(buySpaceJediPackageEventBean.getStage());
        sjPackageTxEntity.setPacakgeCnt(buySpaceJediPackageEventBean.getBuyCnt());
        sjPackageTxService.insert(sjPackageTxEntity);
        return Rsp.ok();
    }

    private int getCurrentStageSold(PresaleStage stage, int totalSold) {
//        switch (stage) {
//            case PRESALE_ROUND_1:
//                return totalSold;
//            case PRESALE_ROUND_2:
//                return totalSold - (PRESALE_ROUND_1.getNumber() + INNER_PRESALE_TOTAL);
//            case PRESALE_ROUND_3:
//                return totalSold - (PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + INNER_PRESALE_TOTAL);
//            case PRESALE_ROUND_4:
//                return totalSold - (PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + PRESALE_ROUND_3.getNumber() + INNER_PRESALE_TOTAL);
//            case PRESALE_ROUND_5:
//                return totalSold - (PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + PRESALE_ROUND_3.getNumber() + PRESALE_ROUND_4.getNumber() + INNER_PRESALE_TOTAL);
//            default:
//                return 0;
//        }
        int[] prefixSum;
        int[] quotas = {
                PRESALE_ROUND_1.getNumber(),
                PRESALE_ROUND_2.getNumber(),
                PRESALE_ROUND_3.getNumber(),
                PRESALE_ROUND_4.getNumber(),
                PRESALE_ROUND_5.getNumber()
        };
        prefixSum = new int[quotas.length];
        prefixSum[0] = INNER_PRESALE_TOTAL; // ROUND_1 之前只有 INNER
        for (int i = 1; i < quotas.length; i++) {
            prefixSum[i] = prefixSum[i - 1] + quotas[i - 1];
        }
        if (stage == PRESALE_ROUND_1) {
            return totalSold;
        }
        int idx = stage.ordinal();
        if (idx == 0 || idx == prefixSum.length) return 0;
        return Math.max(0, totalSold - prefixSum[idx]);
    }


    @GetMapping("/presale/info")
    @ApiOperation("查询预售进度信息")
    public Rsp<PresaleInfoVo> presaleInfo() {
        PresaleInfoVo presaleInfoVo = new PresaleInfoVo();
        presaleInfoVo.setTotal(PRESALE_TOTAL);
        PresaleInfoBean presaleInfoBean = dappPoolService.getPackageCnt();
        int sold = presaleInfoBean.getSold() + INNER_PRESALE_TOTAL;
        presaleInfoVo.setSold(sold);
        double soldPercent = sold * 1.0 / PRESALE_TOTAL;
        presaleInfoVo.setSoldPercent(soldPercent);

        //当前阶段
        PresaleStage currentStage = EnumUtil.fromFieldValue(PresaleStage.class, "stage", presaleInfoBean.getStage());
        if (currentStage == null) {
            throw new BizException(SYSTEM_ERROR, "Presale stage not found: " + presaleInfoBean.getStage());
        }
        PresaleInfoVo.CurrentRoundVo currentRoundVo = new PresaleInfoVo.CurrentRoundVo();
        currentRoundVo.setRound(currentStage.getStage());
        currentRoundVo.setTotal(currentStage.getStage() == 1 ? currentStage.getNumber() + INNER_PRESALE_TOTAL : currentStage.getNumber());
        int currentSold = getCurrentStageSold(currentStage, sold);
        currentRoundVo.setSold(currentSold);
        currentRoundVo.setPrice(currentStage.getPrice().doubleValue());
        double currentSoldPercent = currentStage.getPrice().doubleValue() * PRESALE_SJ_NUMBER_PER;
        currentRoundVo.setCost(currentSoldPercent);
        presaleInfoVo.setCurrentRound(currentRoundVo);

        //下一阶段
        PresaleInfoVo.AfterRoundVo afterRoundVo = new PresaleInfoVo.AfterRoundVo();
        afterRoundVo.setRound(presaleInfoBean.getStage() + 1);
        PresaleStage afterStage = EnumUtil.fromFieldValue(PresaleStage.class, "stage", presaleInfoBean.getStage() + 1);
        if (afterStage == null) {
            throw new BizException(SYSTEM_ERROR, "Presale stage not found: " + presaleInfoBean.getStage());
        }
        afterRoundVo.setTotal(afterStage.getNumber());
        afterRoundVo.setPrice(afterStage.getPrice().doubleValue());
        double afterSoldPercent = afterStage.getPrice().doubleValue() * PRESALE_SJ_NUMBER_PER;
        afterRoundVo.setCost(afterSoldPercent);
        double increasePercentage = (afterStage.getPrice().subtract(currentStage.getPrice())).divide(currentStage.getPrice()).doubleValue();
        afterRoundVo.setIncreasePercentage(increasePercentage);
        presaleInfoVo.setAfterRound(afterRoundVo);

        //各阶段进度
        List<PresaleInfoVo.RoundsVo> rounds = new ArrayList<>();
        for (int i = 1; i <= currentStage.getStage() - 1; i++) {
            PresaleInfoVo.RoundsVo prevRoundsVo = new PresaleInfoVo.RoundsVo();
            PresaleStage iStage = EnumUtil.fromFieldValue(PresaleStage.class, "stage", i);
            if (iStage == null) {
                throw new BizException(SYSTEM_ERROR, "Presale stage not found: " + presaleInfoBean.getStage());
            }
            prevRoundsVo.setTotal(i == 1 ? iStage.getNumber() + INNER_PRESALE_TOTAL : iStage.getNumber());
            prevRoundsVo.setSold(iStage.getNumber());
            prevRoundsVo.setPrice(iStage.getPrice().doubleValue());
            prevRoundsVo.setRound(i);
            prevRoundsVo.setStatus("已完成");
            rounds.add(prevRoundsVo);
        }
        PresaleInfoVo.RoundsVo currentRoundsVo = new PresaleInfoVo.RoundsVo();
        currentRoundsVo.setTotal(currentStage.getStage() == 1 ? currentStage.getNumber() + INNER_PRESALE_TOTAL : currentStage.getNumber());
        currentRoundsVo.setSold(currentSold);
        currentRoundsVo.setPrice(currentStage.getPrice().doubleValue());
        currentRoundsVo.setRound(currentStage.getStage());
        currentRoundsVo.setStatus("进行中");
        rounds.add(currentRoundsVo);
        for (int i = currentStage.getStage() + 1; i <= 5; i++) {
            PresaleInfoVo.RoundsVo afterRoundsVo = new PresaleInfoVo.RoundsVo();
            PresaleStage iStage = EnumUtil.fromFieldValue(PresaleStage.class, "stage", i);
            if (iStage == null) {
                throw new BizException(SYSTEM_ERROR, "Presale stage not found: " + presaleInfoBean.getStage());
            }
            afterRoundsVo.setTotal(iStage.getNumber());
            afterRoundsVo.setSold(0);
            afterRoundsVo.setPrice(iStage.getPrice().doubleValue());
            afterRoundsVo.setRound(i);
            afterRoundsVo.setStatus("未开始");
            rounds.add(afterRoundsVo);
        }
        presaleInfoVo.setRounds(rounds);
        return Rsp.okData(presaleInfoVo);
    }

    @GetMapping("/presale/history")
    @ApiOperation("预售历史记录")
    public Rsp<List<PresaleHistoryVo>> presaleHistory() {
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "create_time"));
        Page<SjPackageTxEntity> pageEntity = sjPackageTxService.queryByPage(new SjPackageTxEntity(), pageRequest);
        if (pageEntity == null || pageEntity.getContent().isEmpty()) {
            return Rsp.okData(Collections.emptyList());
        }
        List<PresaleHistoryVo> presaleHistoryVos = new ArrayList<>();
        for (SjPackageTxEntity sjPackageTxEntity : pageEntity.getContent()) {
            PresaleHistoryVo presaleHistoryVo = new PresaleHistoryVo();
            presaleHistoryVo.setHash(sjPackageTxEntity.getHash());
            presaleHistoryVo.setDatetime(sjPackageTxEntity.getCreateTime());
            presaleHistoryVo.setCost(sjPackageTxEntity.getPrice() * sjPackageTxEntity.getPacakgeCnt() * PRESALE_SJ_NUMBER_PER);
            presaleHistoryVo.setCount(sjPackageTxEntity.getPacakgeCnt());
            presaleHistoryVo.setAddress(sjPackageTxEntity.getAddress());
            presaleHistoryVo.setRound(sjPackageTxEntity.getStage());
            presaleHistoryVos.add(presaleHistoryVo);
        }
        return Rsp.okData(presaleHistoryVos);
    }

//    public static void main(String[] args) {
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_1, 1000 + 100), PRESALE_ROUND_1.getNumber() + INNER_PRESALE_TOTAL);
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_2, 1000 + PRESALE_ROUND_1.getNumber() + 100), PRESALE_ROUND_2.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_3, 1000 + PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + 100), PRESALE_ROUND_3.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_4, 1000 + PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + PRESALE_ROUND_3.getNumber() + 100), PRESALE_ROUND_4.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_5, 1000 + PRESALE_ROUND_1.getNumber() + PRESALE_ROUND_2.getNumber() + PRESALE_ROUND_3.getNumber() + PRESALE_ROUND_4.getNumber() + 100), PRESALE_ROUND_5.getNumber());
//        log.info("/n");
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_1, 1500), PRESALE_ROUND_1.getNumber() + INNER_PRESALE_TOTAL);
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_2, 1900), PRESALE_ROUND_2.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_3, 2300), PRESALE_ROUND_3.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_4, 2650), PRESALE_ROUND_4.getNumber());
//        log.info("{}/{}", getCurrentStageSold(PRESALE_ROUND_5, 3000), PRESALE_ROUND_5.getNumber());
//    }
}
