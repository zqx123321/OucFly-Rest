package cn.ouctechnology.oucfly.rest.interceptor;

import cn.ouctechnology.oucfly.core.OucFly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2019-01-12 14:14
 * @description: TODO
 **/
public class LogInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Object oucFly = request.getAttribute("oucFly");
        if (oucFly != null) {
            String username = ((OucFly) oucFly).getUsername();
            logger.info("{} send request to {}, params are: {}",
                    username, request.getRequestURI(), transferMapToString(request.getParameterMap()));
        }
    }

    private String transferMapToString(Map<String, String[]> parameterMap) {
        StringBuilder sb = new StringBuilder();
        String seq = "";
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String k = entry.getKey();
            String[] v = entry.getValue();
            sb.append(seq).append(k).append("=").append(Arrays.toString(v));
            seq = ",";
        }
        return sb.toString();
    }
}
