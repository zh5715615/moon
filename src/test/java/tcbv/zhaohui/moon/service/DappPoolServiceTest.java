package tcbv.zhaohui.moon.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.config.Web3Config;

import java.math.BigInteger;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceTest
 * @Description:
 * @date: 2025/12/20 18:47
 */
@SpringBootTest
@Slf4j
public class DappPoolServiceTest {

    public static final String TEST_ADDRESS = "0xa24bDb249e80574A96D8B02b148E81B9be684675";

    @Autowired
    private IEthereumService ethereumService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private ICardNFTTokenService cardNFTTokenService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private Web3Config web3Config;

    @BeforeEach
    void init() {
        ethereumService.init();
        spaceJediService.init(web3Config.getSpaceJediContractAddress());
        cardNFTTokenService.init(web3Config.getCardNftContractAddress());
        dappPoolService.init(web3Config.getDappPoolContractAddress());
    }

    @Test
    void extractSpaceJediOnlyTest() {
        try {
            String txHash = dappPoolService.extractSpaceJediOnlyTest(1.0);
            log.info("txHash:{}", txHash);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void submitOrderTest() {
        try {
            String txHash = dappPoolService.submitOrder(TEST_ADDRESS, new BigInteger("92380587157001473839", 10), 256.0);
            log.info("txHash:{}", txHash);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void pareseTradeOrderTest() {
        dappPoolService.parseTradeOrder("0x6af5ccfa0e47bdf9894de3d06a75575eb6d0bcc18806ec2609bea622096344ab");
    }
}
