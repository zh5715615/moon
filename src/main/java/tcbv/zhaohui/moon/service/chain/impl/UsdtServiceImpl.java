package tcbv.zhaohui.moon.service.chain.impl;

import org.springframework.stereotype.Service;
import tcbv.zhaohui.moon.exceptions.ChainException;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;

import static tcbv.zhaohui.moon.exceptions.ChainException.INVOKE_EXCEPTION;

/**
 * @author: zhaohui
 * @Title: UsdtServiceImpl
 * @Description:
 * @date: 2025/12/20 18:26
 */
@Service("usdtService")
public class UsdtServiceImpl extends Token20ServiceImpl {
    @Override
    public String enableLiquidityCreation() throws Exception {
        throw new ChainException(INVOKE_EXCEPTION, "usdt not support liquidity creation");
    }
}
