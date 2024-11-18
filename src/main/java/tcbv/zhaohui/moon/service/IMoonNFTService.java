package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.beans.RecordBean;

import java.math.BigInteger;
import java.util.List;

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
}
