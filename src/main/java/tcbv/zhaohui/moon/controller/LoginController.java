package tcbv.zhaohui.moon.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import tcbv.zhaohui.moon.dto.AdminLoginDto;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.vo.LoginVo;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api/v1/moon/login")
@Slf4j
@Api(tags = "登录相关接口")
public class LoginController {
    @Resource
    private UserInfoService userInfoService;

    @PostMapping("/walletLogin")
    @ApiOperation("钱包登录")
    public Rsp<LoginVo> walletLogin(@RequestBody WalletLoginDto loginDto) {
        return Rsp.okData(userInfoService.walletLogin(loginDto));
    }

    @PostMapping("/admin/login")
    @ApiOperation("管理员登录")
    public Rsp<LoginVo> adminLogin(@RequestBody AdminLoginDto loginDto) {
        if (loginDto.getUsername().equals("admin") && loginDto.getPassword().equals("admin@starwars2026")) {
            return Rsp.okData(userInfoService.adminLogin());
        }
        return Rsp.error("用户名或密码错误");
    }
}
