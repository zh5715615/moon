package tcbv.zhaohui.moon.service.impl;

import tcbv.zhaohui.moon.beans.NFTMetadataBean;
import tcbv.zhaohui.moon.contract.CardNFTToken;
import tcbv.zhaohui.moon.oss.OssConfig;
import tcbv.zhaohui.moon.oss.OssService;
import tcbv.zhaohui.moon.oss.StringMultipartFile;
import tcbv.zhaohui.moon.oss.SysOss;
import tcbv.zhaohui.moon.service.ICardNFTTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import tcbv.zhaohui.moon.utils.EthMathUtil;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.name;

/**
 * @author: zhaohui
 * @Title: CardNFTTokenServiceImpl
 * @Description:
 * @date: 2025/12/12 9:30
 */
@Service
@Slf4j
public class CardNFTTokenServiceImpl extends EthereumService implements ICardNFTTokenService {
    private CardNFTToken cardNFTToken;

    @Autowired
    private OssService ossService;

    @Autowired
    private OssConfig ossConfig;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public void init(String contractAddress) {
        super.init();
        TransactionManager transactionManager = new RawTransactionManager(web3j, credentials, web3Config.getChainId());
        cardNFTToken = CardNFTToken.load(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    @Override
    public long balanceOf(String owner) throws Exception {
        return EthMathUtil.bigIntegerToLong(cardNFTToken.balanceOf(owner).send());
    }

    @Override
    public boolean exists(String tokenId) throws Exception {
        return false;
    }

    @Override
    public String getApproved(String tokenId) throws Exception {
        return null;
    }

    @Override
    public boolean isApprovedForAll(String owner, String operator) throws Exception {
        return false;
    }

    @Override
    public String name() throws Exception {
        return cardNFTToken.name().send();
    }

    @Override
    public String owner() throws Exception {
        return cardNFTToken.owner().send();
    }

    @Override
    public String ownerOf(String tokenId) throws Exception {
        return cardNFTToken.ownerOf(new BigInteger(tokenId, 10)).send();
    }

    @Override
    public boolean pause() throws Exception {
        return false;
    }

    @Override
    public String symbol() throws Exception {
        return cardNFTToken.symbol().send();
    }

    @Override
    public String tokenURI(String tokenId) throws Exception {
        return cardNFTToken.tokenURI(new BigInteger(tokenId, 10)).send();
    }

    @Override
    public String approve(String to, String tokenId) throws Exception {
        return cardNFTToken.approve(to, new BigInteger(tokenId, 10)).send().getTransactionHash();
    }

    @Override
    public String burn(String tokenId) throws Exception {
        return null;
    }

    public static BigInteger random20Digits() {
        // 最小值：10^19（20位里的最小）
        BigInteger min = BigInteger.TEN.pow(19);
        // 最大值：10^20 - 1
        BigInteger max = BigInteger.TEN.pow(20).subtract(BigInteger.ONE);
        BigInteger range = max.subtract(min).add(BigInteger.ONE); // [min, max] 的区间长度

        BigInteger r;
        // 拒绝采样，保证均匀分布
        do {
            r = new BigInteger(range.bitLength(), RANDOM);
        } while (r.compareTo(range) >= 0);

        return r.add(min);
    }

    @Override
    public String mint(String to, NFTMetadataBean nftMetadataBean) throws Exception {
        String metadata = GsonUtil.toJson(nftMetadataBean);
        StringMultipartFile multipartFile = new StringMultipartFile(
                metadata,
                nftMetadataBean.getName() + ".json",
                "application/json"
        );
        String bucketName = "card-nft";
        SysOss sysOss = ossService.upload(bucketName, "metadata", multipartFile);
        BigInteger tokenId = random20Digits();
        String url = ossConfig.getEndpoint() + "/" + bucketName + "/" + sysOss.getFileName();
        return cardNFTToken.mint(to, tokenId, url).send().getTransactionHash();
    }

    @Override
    public String pauseInvoke() throws Exception {
        return null;
    }

    @Override
    public String renounceOwnership() throws Exception {
        return null;
    }

    @Override
    public String safeTransferFrom(String from, String to, String tokenId) throws Exception {
        return cardNFTToken.safeTransferFrom(from, to, new BigInteger(tokenId, 10)).send().getTransactionHash();
    }

    @Override
    public String safeTransferFrom(String from, String to, String tokenId, String data) throws Exception {
        return null;
    }

    @Override
    public String setApprovalForAll(String operator, boolean approved) throws Exception {
        return null;
    }

    @Override
    public String setBaseURI(String baseURI) throws Exception {
        return null;
    }

    @Override
    public String transferFrom(String from, String to, String tokenId) throws Exception {
        return cardNFTToken.transferFrom(from, to, new BigInteger(tokenId, 10)).send().getTransactionHash();
    }

    @Override
    public String unpause() throws Exception {
        return null;
    }
}
