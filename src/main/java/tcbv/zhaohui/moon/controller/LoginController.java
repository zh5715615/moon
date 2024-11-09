package tcbv.zhaohui.moon.controller;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.TbUser;
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
    private TbUserDao tbUserDao;

    @PostMapping("walletLogin")
    @ApiOperation("钱包登录")
    public Rsp<LoginVo> walletLogin(@RequestBody WalletLoginDto loginDto) {
        log.info("登录参数:" + loginDto);
        boolean result = Web3CryptoUtil.validate(loginDto.getSign(), loginDto.getDataSign(),
                loginDto.getAddress());
        if (!result) {
            return Rsp.error("登录签名校验失败");
        }
        String token = UUID.randomUUID().toString();
        TbUser tbUser = tbUserDao.queryByAddress(loginDto.getAddress());
        if(tbUser==null){
            tbUser=new TbUser();
            tbUser.setId(UUID.randomUUID().toString()) ;
            tbUser.setAddress(loginDto.getAddress());
            tbUser.setToken(token);

            tbUserDao.insert(tbUser);
        }
        return Rsp.okData(new LoginVo(loginDto.getAddress(), tbUser.getToken(), tbUser.getId()));
    }
}
