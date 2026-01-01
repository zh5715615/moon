package tcbv.zhaohui.moon.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.service.chain.Token20Service;
import tcbv.zhaohui.moon.service.chain.impl.EthereumServiceImpl;

import java.math.BigDecimal;

/**
 * @author: zhaohui
 * @Title: Token20ServiceTest
 * @Description:
 * @date: 2025/12/20 18:33
 */
@SpringBootTest
@Slf4j
public class Token20ServiceTest {

    public static final String TEST_ADDRESS = "0xa24bDb249e80574A96D8B02b148E81B9be684675";

    @Autowired
    private EthereumServiceImpl ethereumService;

    @Autowired
    @Qualifier("usdtService")
    private Token20Service usdt20Service;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private Web3Config web3Config;

    @BeforeEach
    void init() {
        ethereumService.init();
        usdt20Service.init(ethereumService, web3Config.getUsdtContractAddress());
        spaceJediService.init(ethereumService, web3Config.getSpaceJediContractAddress());
    }

    @Test
    void testBalance() {
        log.info("usdt余额:{}", usdt20Service.balanceOf(TEST_ADDRESS));
        log.info("spaceJedi余额:{}", spaceJediService.balanceOf(TEST_ADDRESS));
    }

    @Test
    void testTransfer() throws Exception {
//        String txHash = usdt20Service.transfer("0x36da0585ce5ca863ac82a736a1edc81bfaa3ba96", 100.0);
//        System.out.println(txHash);
        String txHash = spaceJediService.transfer("0x36da0585ce5ca863ac82a736a1edc81bfaa3ba96", "0x177fbb4590d0d3ed2e7cf6de8e871f9ee5e54302", BigDecimal.valueOf(10.0));
        System.out.println(txHash);
    }
}
