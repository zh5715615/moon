package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.beans.events.BuySpaceJediPackageEventBean;
import tcbv.zhaohui.moon.beans.inputs.NftApproveWithDataInputBean;
import tcbv.zhaohui.moon.dto.TransactionDto;
import tcbv.zhaohui.moon.entity.SjPackageTxEntity;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.SjPackageTxService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.Rsp;

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
}
