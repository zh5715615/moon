package tcbv.zhaohui.moon.service.impl;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import tcbv.zhaohui.moon.dao.TbUserDao;
import tcbv.zhaohui.moon.dto.ConfirmPromoCodeDTO;
import tcbv.zhaohui.moon.dto.FindPromoCodeDTO;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.TbUser;
import tcbv.zhaohui.moon.service.IUSDTLikeInterfaceService;
import tcbv.zhaohui.moon.service.UserInfoService;
import tcbv.zhaohui.moon.utils.Rsp;
import tcbv.zhaohui.moon.utils.Web3CryptoUtil;
import tcbv.zhaohui.moon.vo.LoginVo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

/**
 * @author dawn
 * @date 2024/11/14 9:46
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService {
    private final BigDecimal rewardsMoney = new BigDecimal(100);

    @Resource
    private TbUserDao tbUserDao;

    @Autowired
    private IUSDTLikeInterfaceService iusdtLikeInterfaceService;

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
        TbUser tbUser = tbUserDao.queryByAddress(loginDto.getAddress());
        if (tbUser == null) {
            //查询当前最大的推广码
            Integer code = tbUserDao.maxPromoCode();
            if (code == null || code == 0) {
                code = 213123;
            }
            tbUser = new TbUser();
            tbUser.setId(UUID.randomUUID().toString());
            tbUser.setAddress(loginDto.getAddress());
            tbUser.setToken(token);
            tbUser.setPromoCode(++code);
            tbUserDao.insert(tbUser);
        }
        return new LoginVo(loginDto.getAddress(), tbUser.getToken(), tbUser.getId());
    }

    /**
     * @param dto 查询推广码
     * @return
     */
    @Override
    public TbUser findPromoCode(FindPromoCodeDTO dto) {
        TbUser tbUser = tbUserDao.queryById(dto.getUserId());
        if (tbUser.getPromoCode() == null) {
            Integer code = tbUserDao.maxPromoCode();
            code++;
            tbUser.setPromoCode(code);
            tbUserDao.update(tbUser);
        }
        return tbUser;
    }

    @Override
    @Transactional
    public Boolean confirmPromoCode(ConfirmPromoCodeDTO dto) {
        TbUser tbUser = tbUserDao.queryById(dto.getUserId());
        if (tbUser == null) {
            throw new RuntimeException("查询不到当前用户信息");
        }
        if(StringUtils.isNotBlank(tbUser.getParentId())){
            throw new RuntimeException("当前用户已被推广");
        }
        Integer promoCode = dto.getPromoCode();

        TbUser parentInfo = tbUserDao.promoCodeFindUserInfo(promoCode);
        if (parentInfo == null) {
            throw new RuntimeException("查询不到上级用户信息");
        }
        if(parentInfo.getId().equals(tbUser.getId())){
            throw new RuntimeException("自己不允许推广自己");
        }

        tbUser.setParentId(parentInfo.getId());
        //推广奖励，推广成功后，直推奖励新用户余额的5%, 间推奖励新用户余额的2%，比如A推荐了B，A拿到B余额的5%；B推给C，B拿C余额的5%, A拿C余额的2%；C推给D，那么只有B(2%)和C(5%)可以拿到奖励，A没有.
        try {
            BigDecimal userBalance = iusdtLikeInterfaceService.queryErc20Balance(tbUser.getAddress());
            BigDecimal parentReward = userBalance.multiply(new BigDecimal("0.05"));
            String parentTxHash = iusdtLikeInterfaceService.transfer(parentInfo.getAddress(), parentReward);
            log.info("parentTxHash is {}", parentTxHash);
            String grandParentId = parentInfo.getParentId();
            if (StringUtils.isNotBlank(grandParentId)) {
                TbUser grandParentInfo = tbUserDao.queryById(grandParentId);
                BigDecimal grandParentReward = userBalance.multiply(new BigDecimal("0.02"));
                String grandParentTxHash = iusdtLikeInterfaceService.transfer(grandParentInfo.getAddress(), grandParentReward);
                log.info("grandParentTxHash is {}", grandParentTxHash);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        tbUserDao.update(tbUser);
        return true;
    }


}
