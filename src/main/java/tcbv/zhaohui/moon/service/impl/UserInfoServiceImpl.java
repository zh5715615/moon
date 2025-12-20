package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dao.UserDao;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.UserEntity;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.utils.JwtUtil;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import java.util.Date;
import java.util.HashMap;
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

    @Autowired
    private UserDao userDao;

    @Value("${star-wars.jwt.expired}")
    private long expired;

    /**
     * @param loginDto 钱包登录
     * @return
     */
    @Transactional
    public LoginVo walletLogin(WalletLoginDto loginDto) {
        long now = System.currentTimeMillis() / 1000;
        if (now > loginDto.getTimestamp() + 30 || now < loginDto.getTimestamp() - 30) {
            throw new RuntimeException("登录超时");
        }
        String token = JwtUtil.generateToken(loginDto.getAddress(), expired, new HashMap<>());
        boolean result = Web3CryptoUtil.validate(loginDto.getSign(), loginDto.getDataSign(),
                loginDto.getAddress());
        if (!result) {
            throw new RuntimeException("登录签名校验失败");
        }

        UserEntity userEntity = userDao.queryByAddress(loginDto.getAddress());
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setId(UUID.randomUUID().toString());
            userEntity.setAddress(loginDto.getAddress());
            userEntity.setCreateTime(new Date());
            userDao.insert(userEntity);
        }
        return new LoginVo(loginDto.getAddress(), expired, token);
    }
}
