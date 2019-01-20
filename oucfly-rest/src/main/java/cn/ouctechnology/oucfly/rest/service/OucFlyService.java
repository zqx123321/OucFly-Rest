package cn.ouctechnology.oucfly.rest.service;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import cn.ouctechnology.oucfly.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @program: oucfly-rest
 * @author: ZQX
 * @create: 2018-12-22 14:26
 * @description: TODO
 **/
@Service
@ConfigurationProperties(prefix = "oucfly")
public class OucFlyService {

    @Autowired
    private OucFlyMap oucFlyMap;

    private String mode = "all";

    private int thread = 4;

    private List<String> codes;


    public Result login(String username, String password) {
        String majorCode = username.substring(3, 8);
        if (!majorCode.equals("20031") && !majorCode.equals("20032") && !codes.contains(username)) {
            return Result.fail("为了防止恶意传播，本程序仅限" +
                    "计算机专业(学号为1x020031xxx)以及保密管理专业" +
                    "(学号为1x020032xxx)使用，如果其他专业同学想要使用，" +
                    "请qq联系1127582378");
        }
        OucFly oucFly = new OucFly(username, password, thread, mode);
        Result result = oucFly.init();
        if (!result.isSuccess()) return result;
        String uuid = UUID.randomUUID().toString();
        oucFlyMap.addOucFly(uuid, oucFly);
        return Result.success(uuid);
    }


    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getMode() {
        return mode;
    }

    public int getThread() {
        return thread;
    }

    public Result check(String token) {
        OucFly oucFly = oucFlyMap.getOucFly(token);
        if (oucFly != null) return Result.success();
        return Result.fail();
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }
}
