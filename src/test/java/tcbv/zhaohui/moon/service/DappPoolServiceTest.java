package tcbv.zhaohui.moon.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.config.Web3Config;

/**
 * @author: zhaohui
 * @Title: DappPoolServiceTest
 * @Description:
 * @date: 2025/12/20 18:47
 */
@SpringBootTest
public class DappPoolServiceTest {
    @Autowired
    private IEthereumService ethereumService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private Web3Config web3Config;

    @BeforeEach
    void init() {
        ethereumService.init();
        spaceJediService.init(web3Config.getSpaceJediContractAddress());
        dappPoolService.init(web3Config.getDappPoolContractAddress());
    }

    @Test
    void extractSpaceJediOnlyTest() {
        try {
            dappPoolService.extractSpaceJediOnlyTest(1.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
