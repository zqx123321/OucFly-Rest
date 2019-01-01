package cn.ouctechnology.oucfly.rest.manager;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.exception.OucFlyRestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @program: oucfly-rest
 * @author: ZQX
 * @create: 2018-12-22 13:38
 * @description: TODO
 **/
@Component
@ConfigurationProperties(prefix = "oucfly.limit")
public class OucFlyMap {


    class FlyTime {
        OucFly oucFly;
        long time;

        FlyTime(OucFly oucFly, long time) {
            this.oucFly = oucFly;
            this.time = time;
        }
    }

    private static Logger logger = LoggerFactory.getLogger(OucFlyMap.class);

    private Map<String, FlyTime> oucFlyMap = new HashMap<>();

    private int time = 30 * 60 * 1000;
    private int size = 50;

    public void addOucFly(String uuid, OucFly oucFly) {
        removeTimeOutOucFly();
        if (oucFlyMap.size() > size) {
            String msg = "can not accept more connection, max size is: " + size;
            logger.error(msg);
            throw new OucFlyRestException(msg);
        }
        oucFlyMap.put(uuid, new FlyTime(oucFly, System.currentTimeMillis() + time));
    }

    public OucFly getOucFly(String uuid) {
        removeTimeOutOucFly();
        if (oucFlyMap.containsKey(uuid)) {
            return oucFlyMap.get(uuid).oucFly;
        }
        return null;
    }

    private void removeTimeOutOucFly() {
        long now = System.currentTimeMillis();
        Set<Map.Entry<String, FlyTime>> entries = oucFlyMap.entrySet();
        Iterator<Map.Entry<String, FlyTime>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, FlyTime> flyTime = iterator.next();
            long time = flyTime.getValue().time;
            if (time < now) {
                iterator.remove();
            }
        }
    }

    public boolean containsKey(String token) {
        return oucFlyMap.containsKey(token);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }


}
