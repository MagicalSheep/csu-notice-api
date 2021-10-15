package cn.magicalsheep.csunoticeapi.common.util;

import cn.magicalsheep.csunoticeapi.common.model.Configuration;
import org.springframework.stereotype.Component;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Duration;

@Component
public class Factory {

    private static boolean firstRun = false;
    private static Configuration configuration;
    private static HttpClient httpClient;

    static {
        try {
            configuration = IOUtils.loadConf();
            httpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .cookieHandler(new CookieManager())
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isFirstRun() {
        return firstRun;
    }

    public static void setFirstRun(boolean firstRun) {
        Factory.firstRun = firstRun;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }
}
