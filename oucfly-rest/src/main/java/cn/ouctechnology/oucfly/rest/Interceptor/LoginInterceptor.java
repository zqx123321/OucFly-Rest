package cn.ouctechnology.oucfly.rest.Interceptor;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.exception.LoginException;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE,PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with");
        String uri = request.getRequestURI();
        logger.info("get request: {}", uri);
        String token = request.getParameter("token");
        if (token != null) {
            OucFly oucFly = oucFlyMap.getOucFly(token);
            if (oucFly != null) {
                request.setAttribute("oucFly", oucFly);
                return true;
            }
        }
        throw new LoginException("非法的访问请求: " + uri);
    }
}
