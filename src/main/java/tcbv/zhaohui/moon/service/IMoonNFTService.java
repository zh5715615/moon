package tcbv.zhaohui.moon.service;

import com.google.common.collect.TreeMultimap;
import tcbv.zhaohui.moon.beans.RecordBean;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface IMoonNFTService {
    /**
     * 初始化
     */
    void init(String contractAddress);

    /**
     * 获取nft总数
     *
     * @return nft总数
     */
    long totalSupply() throws Exception;

    /**
     * 分配奖励
     *
     * @param tokenId NFT ID
     * @return 拥有者地址
     */
    String ownerOf(int tokenId) throws Exception;

    /**
     * 获取nft勋章映射列表，key是用户地址，value是勋章数量
     * @return 映射列表
     */
    Map<String, Integer> owners();

    /**
     * 获取nft勋章映射排名列表，从大到小，key是勋章数量，value是用户地址
     * @return 排名列表
     */
    TreeMultimap<Integer, String> nftRank();

    /**
     * 实时排名列表数据写入
     */
    void realtimeRank();
}
