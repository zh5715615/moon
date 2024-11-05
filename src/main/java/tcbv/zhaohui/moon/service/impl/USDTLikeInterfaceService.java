package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.contract.USDTLikeInterface;
import tcbv.zhaohui.moon.service.IMoonBaseService;
import tcbv.zhaohui.moon.service.IUSDTLikeInterfaceService;
import tcbv.zhaohui.moon.utils.EthMathUtil;

@Service
public class USDTLikeInterfaceService extends EthereumService implements IUSDTLikeInterfaceService {

    private USDTLikeInterface usdtLikeInterface;

    public static final int DECIMALS = 18;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        usdtLikeInterface = USDTLikeInterface.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public double queryErc20Balance(String userAddress) throws Exception {
        return EthMathUtil.bigIntegerToDouble(usdtLikeInterface.balanceOf(userAddress).send(), DECIMALS);
    }

    @Override
    public String approve(String spender, double amount) throws Exception {
        return usdtLikeInterface.approve(spender, EthMathUtil.doubleToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transfer(String toAddress, double amount) throws Exception {
        return usdtLikeInterface.transfer(toAddress, EthMathUtil.doubleToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public String transferFrom(String fromAddress, String toAddress, double amount) throws Exception {
        return usdtLikeInterface.transferFrom(fromAddress, toAddress, EthMathUtil.doubleToBigInteger(amount, DECIMALS)).send().getTransactionHash();
    }

    @Override
    public double totalSupply() throws Exception {
        return EthMathUtil.bigIntegerToDouble(usdtLikeInterface.totalSupply().send(), DECIMALS);
    }

    @Override
    public double allowance(String owner, String spender) throws Exception {
        return EthMathUtil.bigIntegerToDouble(usdtLikeInterface.allowance(owner, spender).send(), DECIMALS);
    }
}
