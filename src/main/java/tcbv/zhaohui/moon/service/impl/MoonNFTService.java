package tcbv.zhaohui.moon.service.impl;

import com.google.common.collect.TreeMultimap;
import org.springframework.stereotype.Service;
import org.web3j.tuples.generated.Tuple6;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.beans.RecordBean;
import tcbv.zhaohui.moon.contract.MoonBase;
import tcbv.zhaohui.moon.contract.MoonNFT;
import tcbv.zhaohui.moon.scheduled.TimerMaps;
import tcbv.zhaohui.moon.service.IMoonNFTService;
import tcbv.zhaohui.moon.vo.NFTRankVo;

import java.math.BigInteger;
import java.util.*;

@Service
public class MoonNFTService extends EthereumService implements IMoonNFTService {

    private MoonNFT moonNFT;

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        moonNFT = MoonNFT.load(contractAddress, web3j, transactionManager, contractGasProvider);
        realtimeRank();
    }

    @Override
    public long totalSupply() throws Exception {
        return moonNFT.totalSupply().send().longValue();
    }

    @Override
    public String ownerOf(int tokenId) throws Exception {
        return moonNFT.ownerOf(BigInteger.valueOf(tokenId)).send();
    }

    @Override
    public Map<String, Integer> owners() {
        long total = 0;
        try {
            total = totalSupply();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Map<String, Integer> ownersMap = new HashMap<>();
        for (int i = 1; i <= total; i++) {
            String userAddress;
            try {
                userAddress = ownerOf(i);
            } catch (Exception e) {
                continue;
            }
            if (ownersMap.containsKey(userAddress)) {
                ownersMap.put(userAddress, ownersMap.get(userAddress) + 1);
            } else {
                ownersMap.put(userAddress, 1);
            }
        }
        return ownersMap;
    }

    @Override
    public TreeMultimap<Integer, String> nftRank() {
        Map<String, Integer> inputMap = owners();

        // 创建一个新的 TreeMultimap，并指定键的排序方式为从大到小
        TreeMultimap<Integer, String> multimap = TreeMultimap.create(Collections.reverseOrder(), Comparator.naturalOrder());

        // 遍历输入的 Map，将键值对反转并存入 TreeMultimap 中
        for (Map.Entry<String, Integer> entry : inputMap.entrySet()) {
            multimap.put(entry.getValue(), entry.getKey());
        }

        return multimap;
    }

    @Override
    public void realtimeRank() {
        TreeMultimap<Integer, String> multimap = nftRank();
        TimerMaps.clearNFTRankVoList();
        int i = 1;
        for (Map.Entry<Integer, String> entry : multimap.entries()) {
            NFTRankVo nftRankVo = new NFTRankVo();
            nftRankVo.setNftAmount(entry.getKey());
            nftRankVo.setUserAddress(entry.getValue());
            nftRankVo.setRank(i++);
            TimerMaps.addNFTRankVo(nftRankVo);
        }
    }
}
