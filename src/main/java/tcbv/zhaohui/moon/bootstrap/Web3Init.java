package tcbv.zhaohui.moon.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private ICardNFTTokenService cardNFTTokenService;

    @Autowired
    private Web3Config web3Config;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("================= web3服务初始化开始 =================");
        ethereumService.init();
        cardNFTTokenService.init(web3Config.getCardNftContractAddress());
        log.info("================= web3服务初始化完成 =================");
    }
}
