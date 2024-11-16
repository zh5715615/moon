package tcbv.zhaohui.moon.controller;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.dto.ConfirmPromoCodeDTO;
import tcbv.zhaohui.moon.dto.FindPromoCodeDTO;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import javax.annotation.Resource;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/moon/login")
@Slf4j
public class LoginController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/walletLogin")
    @ApiOperation("钱包登录")
    public Rsp<LoginVo> walletLogin(@RequestBody WalletLoginDto loginDto) {
        return Rsp.okData(userInfoService.walletLogin(loginDto));
    }

    @PostMapping("/findPromoCode")
    @ApiOperation("查询推广码")
    public Rsp<TbUser> findPromoCode(@RequestBody FindPromoCodeDTO dto) {
        TbUser user = userInfoService.findPromoCode(dto);
        TbUser parentUser = userInfoService.findPromoCode(new FindPromoCodeDTO(user.getParentId()));
        user.setParentAddress(parentUser.getAddress());
        return Rsp.okData(user);
    }

    @PostMapping("/confirmPromoCode")
    @ApiOperation("确认推广")
    public Rsp<Boolean> confirmPromoCode(@RequestBody ConfirmPromoCodeDTO dto) {
        return Rsp.okData(userInfoService.confirmPromoCode(dto));
    }
}
