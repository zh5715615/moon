package tcbv.zhaohui.moon.service.chain.impl;

import org.springframework.stereotype.Service;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;

/**
 * @author: zhaohui
 * @Title: SpaceJediSerivceImpl
 * @Description:
 * @date: 2025/12/20 18:27
 */
@Service("spaceJediService")
public class SpaceJediSerivceImpl extends Token20ServiceImpl {
    @Override
    @Web3TxGuard
    public String enableLiquidityCreation() throws Exception {
        return token20Contract.enableLiquidityCreation().send().getTransactionHash();
    }
}
