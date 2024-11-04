package tcbv.zhaohui.moon.bootstrap;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tcbv.zhaohui.moon.service.IEthereumService;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
@Slf4j
public class Web3Init implements ServletContextListener {

    @Autowired
    private IEthereumService ethereumService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        log.info("================= web3服务初始化 =================");
        ethereumService.init();
    }
}
