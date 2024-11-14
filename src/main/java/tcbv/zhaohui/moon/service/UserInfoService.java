package tcbv.zhaohui.moon.service;

import org.springframework.web.bind.annotation.RequestBody;
import tcbv.zhaohui.moon.dto.ConfirmPromoCodeDTO;
import tcbv.zhaohui.moon.dto.FindPromoCodeDTO;
import tcbv.zhaohui.moon.dto.WalletLoginDto;
import tcbv.zhaohui.moon.entity.TbUser;
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
     *
     * @param dto 查询推广码
     * @return
     */
    TbUser findPromoCode( FindPromoCodeDTO dto);

    /**
     *
     * @param dto 确认推广
     * @return
     */
    Boolean confirmPromoCode( ConfirmPromoCodeDTO dto);
}
