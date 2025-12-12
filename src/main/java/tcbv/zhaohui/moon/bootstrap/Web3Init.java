package tcbv.zhaohui.moon.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("================= web3服务初始化 =================");
        ethereumService.init();
        cardNFTTokenService.init("0x6f9cfd50cbf498fa34c08a01f2d6f011795496a9");
    }
}
