package cn.magicalsheep.csunoticeapi;

import cn.magicalsheep.csunoticeapi.common.util.Factory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsuNoticeApiApplication {

    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        ctx = SpringApplication.run(CsuNoticeApiApplication.class, args);
        if (Factory.isFirstRun()) exit();
    }

    public static void exit() {
        ctx.close();
    }
}