package cn.magicalsheep.csunoticeapi;

import cn.magicalsheep.csunoticeapi.http.Connector;
import cn.magicalsheep.csunoticeapi.model.Configuration;
import cn.magicalsheep.csunoticeapi.store.IOHandler;

import java.net.CookieManager;
import java.net.http.HttpClient;
import java.time.Duration;

public class Factory {

    private static boolean firstRun = false;
    private static Configuration configuration;
    private static HttpClient httpClient;
    private static final Connector connector = new Connector();

    static {
        try {
            configuration = IOHandler.loadConf();
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

    public static Connector getConnector() {
        return connector;
    }
}
