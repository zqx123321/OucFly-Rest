package cn.ouctechnology.oucfly.core;

import cn.ouctechnology.oucfly.exception.OucException;
import cn.ouctechnology.oucfly.http.OkHttpUtil;
import cn.ouctechnology.oucfly.operator.Operator;
import cn.ouctechnology.oucfly.operator.login.Login;
import cn.ouctechnology.oucfly.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * @program: oucfly
 * @author: ZQX
 * @create: 2018-12-06 11:51
 * @description: 全局配置对象，所有的操作均需要通过oucFly.run()方法来执行
 * 此对象可以作为全局单例对象，但是系统并不保证长时间不操作知道cookie过期之后
 * 会自动重新获取cookie
 **/
public class OucFly {
    private Logger logger = LoggerFactory.getLogger(OucFly.class);

    //登录学号
    private String username;
    //登录密码
    private String password;
    /**
     * 工作线程数
     */
    private int thread;
    /**
     * 工作时可用的教务处网址
     */
    private HostSet hostSet;

    private OkHttpUtil okHttpUtil;

    private String mode;

    /**
     * 获取下次请求应该请求的url
     *
     * @see HostSet
     */
    public Host getHost() {
        Host host = hostSet.getHost();
        logger.info("get host: {}, connection count: {}", host.getValue(), host.getCount());
        return host;
    }

    public OucFly(String username, String password, int thread, String mode) {
        this.username = username;
        this.password = password;
        this.thread = thread;
        this.mode = mode;
        this.okHttpUtil = new OkHttpUtil();
    }

    public Result init() {
        this.hostSet = new HostSet();
        Login login = new Login(username, password);
        Result<String> res = this.run(login, Host.JWGL_OUC_EDU_CN.getValue());
        if (!res.isSuccess()) return res;
        hostSet.addHost(Host.JWGL_OUC_EDU_CN);
        if (Objects.equals("all", mode)) {
            res = this.run(login, Host.JWGL_2_OUC_EDU_CN.getValue());
            if (res.isSuccess()) hostSet.addHost(Host.JWGL_2_OUC_EDU_CN);
            res = this.run(login, Host.I_222_195_158_206.getValue());
            if (res.isSuccess()) hostSet.addHost(Host.I_222_195_158_206);
            res = this.run(login, Host.I_222_195_158_225.getValue());
            if (res.isSuccess()) hostSet.addHost(Host.I_222_195_158_225);
        }
        if (hostSet.getSize() == 0) return Result.fail("there is no available host");
        String msg = "login in success, there are " + hostSet.getSize() +
                " hosts available, work thread is " + thread;
        logger.info(msg);
        return Result.success(msg);
    }

    /**
     * 核心函数，所有的操作均需要通过此方法来执行
     *
     * @param operator 待执行的操作
     * @param <T>      返回值的类型
     * @return
     */
    public <T> Result<T> run(Operator<T> operator) {
        if (hostSet == null) throw new OucException("must init the oucFly firstly");
        operator.setOucFly(this);
        Host host = getHost();
        host.incr();
        Result<T> res = operator.run(host.getValue());
        host.decr();
        return res;
    }

    /**
     * 指定host 的run方法
     */
    public <T> Result<T> run(Operator<T> operator, String host) {
        if (hostSet == null) throw new OucException("must init the oucFly firstly");
        operator.setOucFly(this);
        return operator.run(host);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getThread() {
        return thread;
    }

    public OkHttpUtil getOkHttpUtil() {
        return okHttpUtil;
    }
}
