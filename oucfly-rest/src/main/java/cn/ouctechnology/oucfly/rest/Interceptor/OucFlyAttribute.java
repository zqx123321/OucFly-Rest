package cn.ouctechnology.oucfly.rest.Interceptor;

import java.lang.annotation.*;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-23 15:20
 * @description: TODO
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OucFlyAttribute {
}