package tcbv.zhaohui.moon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.contract.DappPool;
import tcbv.zhaohui.moon.exceptions.Web3TxGuard;
import tcbv.zhaohui.moon.service.DappPoolService;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import tcbv.zhaohui.moon.service.Token20Service;
import tcbv.zhaohui.moon.utils.AbiEventLogDecoder;
import tcbv.zhaohui.moon.utils.AbiInputDecoder;
import tcbv.zhaohui.moon.utils.EthMathUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.math.BigInteger;
import java.util.List;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceImpl
 * @Description:
 * @date: 2025/12/20 17:48
 */
@Service
@Slf4j
public class DappPoolServiceImpl extends EthereumService implements DappPoolService {
    private DappPool dappPool;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private ICardNFTTokenService cardNFTTokenService;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        dappPool = DappPool.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    @Web3TxGuard
    public String extractSpaceJediOnlyTest(Double amount) throws Exception {
        BigInteger amountWei = EthMathUtil.doubleToBigInteger(amount, spaceJediService.getDecimals());
        return dappPool.extractSpaceJediOnlyTest(amountWei).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String submitOrder(String seller, BigInteger tokenId, Double price) throws Exception {
        BigInteger priceWei = EthMathUtil.doubleToBigInteger(price, spaceJediService.getDecimals());
        boolean exists = cardNFTTokenService.exists(tokenId.toString(10));
        if (!exists) {
            log.error("tokenId {} not exists", tokenId);
            return null;
        }
        return dappPool.submitOrder(seller, tokenId, priceWei).send().getTransactionHash();
    }

    @Override
    @Web3TxGuard
    public String cancelOrder(String owner, BigInteger tokenId) throws Exception {
        return dappPool.cancelOrder(owner, tokenId).send().getTransactionHash();
    }

    @Override
    public void parseTradeOrder(String txHash) {
        String abiJson = "[{\"inputs\":[{\"internalType\":\"address\",\"name\":\"beneficiary\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_usdtAddress\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_spaceJediAddress\",\"type\":\"address\"},{\"internalType\":\"address\",\"name\":\"_cardNftAddress\",\"type\":\"address\"}],\"stateMutability\":\"payable\",\"type\":\"constructor\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"}],\"name\":\"OwnableInvalidOwner\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"OwnableUnauthorizedAccount\",\"type\":\"error\"},{\"inputs\":[],\"name\":\"ReentrancyGuardReentrantCall\",\"type\":\"error\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"token\",\"type\":\"address\"}],\"name\":\"SafeERC20FailedOperation\",\"type\":\"error\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"buyer\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"packageCnt\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"totalCost\",\"type\":\"uint256\"}],\"name\":\"BuySpaceJediPackage\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"}],\"name\":\"CancelOrder\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"previousOwner\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"OwnershipTransferred\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"level\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Pledge\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"price\",\"type\":\"uint256\"}],\"name\":\"SubmitOrder\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"seller\",\"type\":\"address\"},{\"indexed\":true,\"internalType\":\"address\",\"name\":\"buyer\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"price\",\"type\":\"uint256\"}],\"name\":\"TradeOrder\",\"type\":\"event\"},{\"anonymous\":false,\"inputs\":[{\"indexed\":true,\"internalType\":\"address\",\"name\":\"user\",\"type\":\"address\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"level\",\"type\":\"uint256\"},{\"indexed\":false,\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"Withdraw\",\"type\":\"event\"},{\"inputs\":[],\"name\":\"buySpaceJediPackage\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"}],\"name\":\"cancelOrder\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"cardNftToken\",\"outputs\":[{\"internalType\":\"contract IERC721\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"amount\",\"type\":\"uint256\"}],\"name\":\"extractSpaceJediOnlyTest\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"}],\"name\":\"getCurrentRewardPercent\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"poolBalance\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"rewardPercentX10\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getPackageCnt\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"},{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"getPledgedAmount\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"},{\"internalType\":\"address\",\"name\":\"account\",\"type\":\"address\"}],\"name\":\"getPledgedTime\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"}],\"name\":\"getRegion\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"pledgedAmount\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"pledgedPeriod\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"rewardPercent\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"getUserBuyPackageCnt\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"owner\",\"outputs\":[{\"internalType\":\"address\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"}],\"name\":\"pledge\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"poolInitAmountWei\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"renounceOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"sjTokenDecimals\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"spaceJediToken\",\"outputs\":[{\"internalType\":\"contract IERC20Metadata\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"owner\",\"type\":\"address\"},{\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"},{\"internalType\":\"uint256\",\"name\":\"price\",\"type\":\"uint256\"}],\"name\":\"submitOrder\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"uint256\",\"name\":\"tokenId\",\"type\":\"uint256\"}],\"name\":\"tradeOrder\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"address\",\"name\":\"newOwner\",\"type\":\"address\"}],\"name\":\"transferOwnership\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"usdtToken\",\"outputs\":[{\"internalType\":\"contract IERC20Metadata\",\"name\":\"\",\"type\":\"address\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[],\"name\":\"usdtTokenDecimals\",\"outputs\":[{\"internalType\":\"uint256\",\"name\":\"\",\"type\":\"uint256\"}],\"stateMutability\":\"view\",\"type\":\"function\"},{\"inputs\":[{\"internalType\":\"enum DappPool.Level\",\"name\":\"level\",\"type\":\"uint8\"}],\"name\":\"withdraw\",\"outputs\":[],\"stateMutability\":\"nonpayable\",\"type\":\"function\"}]";
        try {
            AbiInputDecoder.DecodedCall call = AbiInputDecoder.decodeTxHash(web3j, txHash, abiJson);
            if (call.getFunctionName().equals(DappPool.FUNC_TRADEORDER)) {
                log.info("Input Args: {}", GsonUtil.toJson(call.getArgs()));
            }

            List<AbiEventLogDecoder.DecodedEvent> events =
                    AbiEventLogDecoder.decodeTxEvents(web3j, txHash, abiJson);
            for (AbiEventLogDecoder.DecodedEvent event : events) {
                if (event.getEventName().equals(DappPool.TRADEORDER_EVENT.getName())) {
                    log.info("Log Args: {}", GsonUtil.toJson(event.getArgs()));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
