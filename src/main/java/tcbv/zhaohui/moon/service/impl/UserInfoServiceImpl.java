package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author dawn
 * @date 2024/11/14 9:46
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private Web3Config web3Config;

    /**
     * @param loginDto 钱包登录
     * @return
     */
    @Transactional
    public LoginVo walletLogin(WalletLoginDto loginDto) {
        log.info("登录参数:" + loginDto);
        boolean result = Web3CryptoUtil.validate(loginDto.getSign(), loginDto.getDataSign(),
                loginDto.getAddress());
        if (!result) {
            throw new RuntimeException("登录签名校验失败");
        }
        String token = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

//        TbUser tbUser = tbUserDao.queryByAddress(loginDto.getAddress());
//        if (tbUser == null) {
//            //查询当前最大的推广码
//            Integer code = tbUserDao.maxPromoCode();
//            if (code == null || code == 0) {
//                code = 213123;
//            }
//            tbUser = new TbUser();
//            tbUser.setId(UUID.randomUUID().toString());
//            tbUser.setAddress(loginDto.getAddress());
//            tbUser.setToken(token);
//            tbUser.setPromoCode(++code);
//            tbUserDao.insert(tbUser);
//        }
        return new LoginVo(loginDto.getAddress(), userId, token);
    }
}
