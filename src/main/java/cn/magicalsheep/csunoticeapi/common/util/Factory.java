package cn.magicalsheep.csunoticeapi.common.util;

import cn.magicalsheep.csunoticeapi.common.model.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class Factory {

    private static final Logger logger = LoggerFactory.getLogger(Factory.class);
    private static HttpClient httpClient;
    private static int updateNum = 0;

    static {
        try {
            if (Configuration.getBooleanProperties("initialization")) {
                if (Configuration.getBooleanProperties("update_school_notice"))
                    ++updateNum;
                if (Configuration.getBooleanProperties("update_cse_notice"))
                    ++updateNum;
                if (Configuration.getBooleanProperties("update_headmaster_mail"))
                    ++updateNum;
            }
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .cookieHandler(new CookieManager())
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void updateAllCompletedFallback() {
        --updateNum;
        if (updateNum == 0) {
            Configuration.setProperties("initialization", false);
            logger.info("Setting field init_db has been changed to false");
            try {
                Configuration.save();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

}
