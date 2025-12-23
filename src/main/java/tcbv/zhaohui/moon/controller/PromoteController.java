package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.jwt.JwtAddressRequired;
import tcbv.zhaohui.moon.jwt.JwtContext;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.syslog.Syslog;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.LoginVo;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/moon/promote")
@Slf4j
@Api(tags = "推广相关接口")
public class PromoteController {
    @Resource
    private UserInfoService userInfoService;

    @Syslog(module = "PROMOTE")
    @PutMapping("/link/{promoCode}")
    @ApiOperation("点击推广链接")
    @JwtAddressRequired
    public Rsp walletLogin(@PathVariable("promoCode") String promoCode) {
        String userId = JwtContext.getUserId();
        String errMsg = userInfoService.bindPromoter(userId, promoCode);
        if (null != errMsg) {
            return Rsp.error(errMsg);
        }
        return Rsp.ok();
    }
}
