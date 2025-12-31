package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import tcbv.zhaohui.moon.vo.PresaleInfoVo;

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
        sjPackageTxEntity.setPrice(buySpaceJediPackageEventBean.getPrice());
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

        PresaleStage currentStage = EnumUtil.fromFieldValue(PresaleStage.class, "stage", presaleInfoBean.getStage());
        if (currentStage == null) {
            throw new BizException(SYSTEM_ERROR, "Presale stage not found: " + presaleInfoBean.getStage());
        }
        PresaleInfoVo.CurrentRoundVo currentRoundVo = new PresaleInfoVo.CurrentRoundVo();
        currentRoundVo.setRound(currentStage.getStage());
        currentRoundVo.setTotal(currentStage.getNumber());
        int currentSold = getCurrentStageSold(currentStage, sold);
        currentRoundVo.setSold(currentSold);
        currentRoundVo.setPrice(currentStage.getPrice());
        double currentSoldPercent = currentStage.getPrice() * PRESALE_SJ_NUMBER_PER;
        currentRoundVo.setCost(currentSoldPercent);
        presaleInfoVo.setCurrentRound(currentRoundVo);
        return Rsp.okData(presaleInfoVo);
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
