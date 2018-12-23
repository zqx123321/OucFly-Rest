package cn.ouctechnology.oucfly.rest.config;

import cn.ouctechnology.oucfly.rest.Interceptor.LoginInterceptor;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-23 17:24
 * @description: TODO
 **/
@Configuration
public class InterceptorConfig {
    @Bean
    public LoginInterceptor localInterceptor(OucFlyMap oucFlyMap) {
        LoginInterceptor loginInterceptor = new LoginInterceptor(oucFlyMap);
        return loginInterceptor;
    }
}
