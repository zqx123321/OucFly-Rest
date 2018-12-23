package cn.ouctechnology.oucfly.rest.exception;

import cn.ouctechnology.oucfly.exception.OucException;
import cn.ouctechnology.oucfly.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-23 12:18
 * @description: TODO
 **/
@ControllerAdvice
@ResponseBody
public class ExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(LoginException.class)
    public Result handleException(LoginException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(OucException.class)
    public Result handleException(OucException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(OucFlyRestException.class)
    public Result handleException(OucFlyRestException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result handleException(ConstraintViolationException e) {
        logger.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return Result.fail("未知错误");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        logger.error(e.getMessage(), e);
        return Result.fail("必须使用POST方法");
    }
}
