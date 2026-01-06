package tcbv.zhaohui.moon.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: zhaohui
 * @Title: HttpsConfig
 * @Description:
 * @date: 2026/1/6 19:16
 */
@Configuration
public class HttpsConfig {

    @Value("${http.port:8082}")
    private int httpPort;

    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        // 添加 HTTP 连接器
        tomcat.addAdditionalTomcatConnectors(createHttpConnector());

        return tomcat;
    }

    private Connector createHttpConnector() {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setScheme("http");
        connector.setSecure(false);
        connector.setPort(httpPort); // 例如 8080
        return connector;
    }
}