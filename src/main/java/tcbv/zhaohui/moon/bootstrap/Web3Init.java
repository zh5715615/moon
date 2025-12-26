package tcbv.zhaohui.moon.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.config.Web3Config;
import tcbv.zhaohui.moon.service.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
@Slf4j
public class Web3Init implements ServletContextListener {

    @Autowired
    private IEthereumService ethereumService;

    @Autowired
    @Qualifier("usdtService")
    private Token20Service usdtService;

    @Autowired
    @Qualifier("spaceJediService")
    private Token20Service spaceJediService;

    @Autowired
    private ICardNFTTokenService cardNFTTokenService;

    @Autowired
    private DappPoolService dappPoolService;

    @Autowired
    private Web3Config web3Config;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("================= web3服务初始化开始 =================");
        ethereumService.init();
        usdtService.init(ethereumService, web3Config.getUsdtContractAddress());
        spaceJediService.init(ethereumService, web3Config.getSpaceJediContractAddress());
        cardNFTTokenService.init(ethereumService, web3Config.getCardNftContractAddress());
        dappPoolService.init(ethereumService, web3Config.getDappPoolContractAddress());
        log.info("================= web3服务初始化完成 =================");
    }
}
