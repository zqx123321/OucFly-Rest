package cn.ouctechnology.oucfly.rest.Interceptor;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.exception.LoginException;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-23 12:00
 * @description: TODO
 **/
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);

    private OucFlyMap oucFlyMap;

    public LoginInterceptor(OucFlyMap oucFlyMap) {
        this.oucFlyMap = oucFlyMap;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        logger.info("get request: {}", uri);
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (Objects.equals("token", name)) {
                String uuid = cookie.getValue();
                OucFly oucFly = oucFlyMap.getOucFly(uuid);
                if (oucFly != null) {
                    request.setAttribute("oucFly", oucFly);
                    return true;
                }
            }
        }
        throw new LoginException("非法的访问请求: " + uri);
    }
}
