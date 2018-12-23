package cn.ouctechnology.oucfly.rest.exception;

/**
 * @program: oucfly-rest
 * @author: ZQX
 * @create: 2018-12-22 13:50
 * @description: TODO
 **/
public class OucFlyRestException extends RuntimeException{
    public OucFlyRestException() {
        super();
    }

    public OucFlyRestException(String message) {
        super(message);
    }

    public OucFlyRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public OucFlyRestException(Throwable cause) {
        super(cause);
    }

    protected OucFlyRestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
