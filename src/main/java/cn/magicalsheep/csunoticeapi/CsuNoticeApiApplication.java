package cn.magicalsheep.csunoticeapi;

import cn.magicalsheep.csunoticeapi.common.util.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsuNoticeApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(CsuNoticeApiApplication.class);
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        if (Factory.isFirstRun()) {
            logger.error("Please finish the settings.json to run API");
            System.exit(0);
        }
        ctx = SpringApplication.run(CsuNoticeApiApplication.class, args);
    }

    public static void exit() {
        ctx.close();
    }
}