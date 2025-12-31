package tcbv.zhaohui.moon.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tcbv.zhaohui.moon.beans.BlockInfoBean;
import tcbv.zhaohui.moon.beans.TransactionBean;
import tcbv.zhaohui.moon.service.chain.EthereumService;
import tcbv.zhaohui.moon.utils.GsonUtil;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
public class EthereumServiceImplTest {

    @Autowired
    private EthereumService ethereumService;

    @BeforeEach
    void before() {
        ethereumService.init();
    }

    @Test
    void testBestBlockNumber() {
        long blockNumber = ethereumService.lastHeigh();
        log.info("Best blocknumber is {}", blockNumber);
        assertTrue(blockNumber != 0);
    }

    @Test
    void testBlockInfo() throws IOException {
        BlockInfoBean blockInfoBean = ethereumService.getBlockInfo(10661248);
        assertNotNull(blockInfoBean);
        log.info("BlockInfo is {}", GsonUtil.toJson(blockInfoBean));
    }

    @Test
    void testTransactionInfo() throws IOException {
        TransactionBean transactionBean = ethereumService.getTransactionInfo("0x7aa31a00d5f793567e616f7fd4ae56afdaac162b0995493cd3ff1b676c94d386");
        assertNotNull(transactionBean);
        log.info("Transaction is {}", GsonUtil.toJson(transactionBean));
    }

    @Test
    void testGetBalance() {
        String accountAddress = "0x0D0707963952f2fBA59dD06f2b425ace40b492Fe";
        double balance = ethereumService.getEthBalance(accountAddress);
        log.info("Account {} balance is {}", accountAddress, balance);
    }

    @Test
    void testGetAccountNonce() throws IOException {
        String accountAddress = "0x0D0707963952f2fBA59dD06f2b425ace40b492Fe";
        long nonce = ethereumService.getAccountNonce(accountAddress);
        log.info("Account {} nonce is {}", accountAddress, nonce);
    }

    @Test
    void testSendEth() throws IOException {
        String to = "0xab429a6547EC5aC5754B0b63500e32e5A932D915";
        double amount = 1;
        String txHash = ethereumService.sendEth(to, amount);
        log.info("txHash is {}", txHash);
    }
}
