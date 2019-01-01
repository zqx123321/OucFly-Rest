package cn.ouctechnology.oucfly.rest.service;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import cn.ouctechnology.oucfly.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

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


    public Result login(String username, String password) {
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
}
