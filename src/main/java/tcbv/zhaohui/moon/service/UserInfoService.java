package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.vo.LoginVo;

/**
 * @author dawn
 * @date 2024/11/14 9:45
 */
public interface UserInfoService {
    /**
     *
     * @param loginDto 钱包登录
     * @return
     */
    LoginVo walletLogin(WalletLoginDto loginDto);

    /**
     * 绑定推广人
     * @param userId 用户id
     * @param promoCode 推广码
     */
    String bindPromoter(String userId, String promoCode);
}
