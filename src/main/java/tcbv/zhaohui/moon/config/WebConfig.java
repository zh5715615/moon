package tcbv.zhaohui.moon.config;

/**
 * @author: zhaohui
 * @Title: WebConfig
 * @Description:
 * @date: 2025/12/21 9:49
 */
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tcbv.zhaohui.moon.jwt.JwtAddressInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAddressInterceptor())
                .addPathPatterns("/**"); // 或者只拦 /api/**
    }
}
