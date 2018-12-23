package cn.ouctechnology.oucfly.rest;

import cn.ouctechnology.oucfly.core.OucFly;
import cn.ouctechnology.oucfly.rest.manager.OucFlyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@SpringBootApplication
@Component
public class OucflyRestApplication {


    public static void main(String[] args) {
        SpringApplication.run(OucflyRestApplication.class, args);
    }

}

