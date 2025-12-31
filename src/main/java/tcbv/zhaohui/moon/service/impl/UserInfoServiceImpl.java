package tcbv.zhaohui.moon.service.impl;

import cn.hutool.core.codec.Base62;
import cn.hutool.core.util.HexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.dao.UserDao;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.UserEntity;
import tcbv.zhaohui.moon.exceptions.BizException;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.jwt.JwtUtil;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static tcbv.zhaohui.moon.exceptions.BizException.LOGIN_TIMEOUT;
import static tcbv.zhaohui.moon.exceptions.BizException.LOGIN_VALID_FAILED;

/**
 * @author dawn
 * @date 2024/11/14 9:46
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserDao userDao;

    @Value("${star-wars.jwt.expired}")
    private long expired;

    @Value("${star-wars.login.timestamp-check}")
    private boolean timestampCheck;

    /**
     * @param loginDto 钱包登录
     * @return
     */
    @Transactional
    public LoginVo walletLogin(WalletLoginDto loginDto) {
        if (timestampCheck) {
            long now = System.currentTimeMillis() / 1000;
            if (now > loginDto.getTimestamp() + 30 || now < loginDto.getTimestamp() - 30) {
                throw new BizException(LOGIN_TIMEOUT, "登录超时");
            }
        }

        boolean result = Web3CryptoUtil.validate(loginDto.getSign(), loginDto.getDataSign(),
                loginDto.getAddress());
        if (!result) {
            throw new BizException(LOGIN_VALID_FAILED, "登录签名校验失败");
        }

        UserEntity userEntity = userDao.queryByAddress(loginDto.getAddress());
        if (userEntity == null) {
            userEntity = new UserEntity();
            userEntity.setId(UUID.randomUUID().toString());
            userEntity.setAddress(loginDto.getAddress());
            String uuid = UUID.randomUUID().toString().replace("-", "");
            byte[] uuidBytes = HexUtil.decodeHex(uuid);
            userEntity.setPromoCode(Base62.encode(uuidBytes));
            userEntity.setCreateTime(new Date());
            userDao.insert(userEntity);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("address", loginDto.getAddress());
        String token = JwtUtil.generateToken(userEntity.getId(), expired, params);
        String promoteLink = "http://api/v1/moon/promote/link/" + userEntity.getPromoCode();
        return new LoginVo(loginDto.getAddress(), expired, token, promoteLink);
    }

    @Override
    public String bindPromoter(String userId, String promoCode) {
        UserEntity parentEntity = userDao.queryByPromoCode(promoCode);
        if (parentEntity == null) {
            return "推广码对应推广人用户不存在";
        }
        UserEntity userEntity = userDao.queryById(userId);
        if (StringUtils.isNotBlank(userEntity.getParentId())) {
            return "您已经绑定过推广人";
        }
        String pid = parentEntity.getId();
        if (userId.equals(pid)) {
            return "推广人不能是自己";
        }
        userDao.bindPromoter(userId, pid);
        return null;
    }
}
