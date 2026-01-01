package tcbv.zhaohui.moon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.service.chain.CardNFTTokenService;
import tcbv.zhaohui.moon.service.chain.DappPoolService;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.service.chain.impl.EthereumServiceImpl;
import tcbv.zhaohui.moon.utils.EthMathUtil;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: CardNFTTokenServiceTest
 * @Description:
 * @date: 2025/12/20 21:20
 */
@SpringBootTest
public class CardNFTTokenServiceTest {
    public static final String TEST_ADDRESS = "0xa24bDb249e80574A96D8B02b148E81B9be684675";

    @Autowired
    private EthereumServiceImpl ethereumService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private CardNFTTokenService cardNFTTokenService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private Web3Config web3Config;

    @BeforeEach
    void init() {
        ethereumService.init();
        spaceJediService.init(ethereumService, web3Config.getSpaceJediContractAddress());
        cardNFTTokenService.init(ethereumService, web3Config.getCardNftContractAddress());
        dappPoolService.init(ethereumService, web3Config.getDappPoolContractAddress());
    }

    @Test
    void approveTest() throws Exception {
        BigDecimal price = BigDecimal.valueOf(0x100 * 1.0);
        BigInteger priceInWei = EthMathUtil.decimalToBigInteger(price, spaceJediService.getDecimals());
        byte[] data = priceInWei.toByteArray();
        cardNFTTokenService.approveWithData(web3Config.getDappPoolContractAddress(), "92380587157001473839", data);
    }
}
