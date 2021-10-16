package cn.magicalsheep.csunoticeapi;

import cn.magicalsheep.csunoticeapi.common.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;

@SpringBootApplication
@EnableScheduling
public class CsuNoticeApiApplication {

    private static final Logger logger = LoggerFactory.getLogger(CsuNoticeApiApplication.class);
    private static ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        File settings = new File(Configuration.getPath());
        try {
            if (!settings.exists()) {
                logger.error("Please finish the settings.properties to run API");
                Configuration.init();
                Configuration.save();
                logger.info("Create settings.properties successfully");
                System.exit(0);
            } else {
                Configuration.load();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
        ctx = SpringApplication.run(CsuNoticeApiApplication.class, args);
    }

    public static void exit() {
        ctx.close();
    }
}