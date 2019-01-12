package cn.ouctechnology.oucfly.rest.interceptor;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-23 15:20
 * @description: TODO
 **/

public class OucFlyAttributeMethodResolver implements HandlerMethodArgumentResolver {
    /**
     * 检查解析器是否支持解析改参数
     */
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(OucFlyAttribute.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        return nativeWebRequest.getAttribute("oucFly", WebRequest.SCOPE_REQUEST);
    }
}
