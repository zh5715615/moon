package tcbv.zhaohui.moon.service.impl;

import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.RecordBean;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.contract.MoonNFT;
import tcbv.zhaohui.moon.service.IMoonNFTService;

import java.math.BigInteger;
import java.util.List;

@Service
public class MoonNFTService extends EthereumService implements IMoonNFTService {

    private MoonNFT moonNFT;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        moonNFT = MoonNFT.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public long totalSupply() throws Exception {
        return moonNFT.totalSupply().send().longValue();
    }

    @Override
    public String ownerOf(int tokenId) throws Exception {
        return moonNFT.ownerOf(BigInteger.valueOf(tokenId)).send();
    }


}
