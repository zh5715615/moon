package tcbv.zhaohui.moon.controller;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/moon/login")
@Slf4j
public class LoginController {
    @PostMapping("walletLogin")
    @ApiOperation("钱包登录")
    public Rsp<LoginVo> walletLogin(@RequestBody WalletLoginDto loginDto) {
        log.info("登录参数:" + loginDto);
        boolean result = Web3CryptoUtil.validate(loginDto.getSign(), loginDto.getDataSign(),
                loginDto.getAddress());
        if (!result) {
            return Rsp.error("登录签名校验失败");
        }
        String token = "test"; //TODO 这个地方需要给个token存到user表，为后续操作提供token
        //TODO 下面还要添加一个逻辑，user表有则更新token，无则插入新数据
        return Rsp.okData(new LoginVo(loginDto.getAddress(), token));
    }
}
