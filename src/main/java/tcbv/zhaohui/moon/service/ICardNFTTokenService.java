package tcbv.zhaohui.moon.service;

import tcbv.zhaohui.moon.beans.NFTMetadataBean;
import tcbv.zhaohui.moon.beans.NFTTokenMintInfoBean;
import tcbv.zhaohui.moon.beans.events.SubmitOrderEventBean;
import tcbv.zhaohui.moon.beans.inputs.NftApproveWithDataInputBean;
import tcbv.zhaohui.moon.service.impl.EthereumService;

/**
 * @author: zhaohui
 * @Title: IToken721Service
 * @Description:
 * @date: 2025/12/12 9:27
 */
public interface ICardNFTTokenService {
    void init(IEthereumService ethereumService, String contractAddress);

    long balanceOf(String owner) throws Exception;

    boolean exists(String tokenId) throws Exception;

    String getApproved(String tokenId) throws Exception;

    boolean isApprovedForAll(String owner, String operator) throws Exception;

    String name() throws Exception;

    String owner() throws Exception;

    String ownerOf(String tokenId) throws Exception;

    boolean pause() throws Exception;

    String symbol() throws Exception;

    String tokenURI(String tokenId) throws Exception;

    String approve(String to, String tokenId) throws Exception;

    String approve(String from, String to, String tokenId) throws Exception;

    String burn(String tokenId) throws Exception;

    NFTTokenMintInfoBean mint(String to, NFTMetadataBean nftMetadataBean) throws Exception;

    String pauseInvoke() throws Exception;

    String renounceOwnership() throws Exception;

    String safeTransferFrom(String from, String to, String tokenId) throws Exception;

    String safeTransferFrom(String from, String to, String tokenId, String data) throws Exception;

    String setApprovalForAll(String operator, boolean approved) throws Exception;

    String setBaseURI(String baseURI) throws Exception;

    String transferFrom(String from, String to, String tokenId) throws Exception;

    String unpause() throws Exception;

    String approveWithData(String to, String tokenId, byte[] data) throws Exception;
}
