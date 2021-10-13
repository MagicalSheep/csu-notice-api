package cn.magicalsheep.csunoticeapi.util;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.model.packet.Packet;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class HttpUtils implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final ChromeDriverService chromeDriverService;
    private static final ChromeOptions chromeOptions = new ChromeOptions();
    private static final WebDriver driver;

    static {
        chromeOptions.setBinary(Factory.getConfiguration().getChrome_path());
        chromeOptions.addArguments("--headless");
        chromeDriverService = new ChromeDriverService.Builder()
                .usingAnyFreePort()
                .build();
        try {
            chromeDriverService.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        driver = new RemoteWebDriver(chromeDriverService.getUrl(), chromeOptions);
    }

    public static URI getURI(String uri) {
        return getURI(uri, null);
    }

    public static URI getURI(String uri, Packet packet) {
        URI ret = null;
        if (packet == null) {
            ret = URI.create(uri);
        } else if (packet.getType() == Packet.TYPE.LOGIN) {
            ret = URI.create(String.format(uri + "?%s=%s&%s=%s", "user", ((LoginPacket) packet).getUser(), "pwd", ((LoginPacket) packet).getPwd()));
        }
        return ret;
    }

    public static HttpResponse<String> get(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .setHeader("User-Agent", "CSU Notice API Bot")
                .build();
        return Factory.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Get snapshot from an uri
     *
     * @param uri uri
     * @return Base64 image
     */
    public static String snapshot(String uri) {
        driver.get(uri);
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    /**
     * Using browser to get resources from an uri
     * REMEMBER CLOSE WINDOW AFTER USING
     *
     * @param uri uri
     * @return html
     */
    public static String getByBrowser(URI uri) {
        driver.get(uri.toString());
        return driver.getPageSource();
    }

    @Override
    public void destroy() {
        driver.quit();
        chromeDriverService.stop();
    }
}
