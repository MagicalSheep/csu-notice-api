package cn.magicalsheep.csunoticeapi.util;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.model.packet.Packet;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Component
public class HttpUtils implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final ChromeDriverService chromeDriverService;
    private static final ChromeOptions chromeOptions = new ChromeOptions();
    private static final AShot ashot;

    static {
        chromeOptions.setBinary(Factory.getConfiguration().getChrome_path());
        chromeOptions.addArguments("headless");
        chromeOptions.addArguments("window-size=1920,1080");
        ashot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000));
        chromeDriverService = new ChromeDriverService.Builder()
                .usingAnyFreePort()
                .build();
        try {
            chromeDriverService.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
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

    public static WebDriver createDriver(){
        return new RemoteWebDriver(chromeDriverService.getUrl(), chromeOptions);
    }

    /**
     * Get snapshot from an uri
     *
     * @param webDriver webDriver
     * @param uri uri
     * @return Base64 image
     */
    public static String snapshot(WebDriver webDriver, String uri) {
        try
        {
            webDriver.get(uri);
            Thread.sleep(2000L);
            Screenshot screenshot = ashot.takeScreenshot(webDriver);
            return imgToBase64String(screenshot.getImage());
        } catch (InterruptedException e)
        {
            logger.error(e.getMessage());
            return null;
        }
    }

    private static String imgToBase64String(BufferedImage image) {
        String ret;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", bos);
            byte[] imageBytes = bos.toByteArray();
            Base64.Encoder encoder = Base64.getEncoder();
            ret = encoder.encodeToString(imageBytes);
            bos.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
        return ret;
    }

    private static BufferedImage base64StringToImg(String imageString) {
        BufferedImage image = null;
        byte[] imageByte;
        try {
            Base64.Decoder decoder = Base64.getDecoder();
            imageByte = decoder.decode(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * Using browser to get resources from an uri
     *
     * @param webDriver webDriver
     * @param uri uri
     * @return html
     */
    public static String getByBrowser(WebDriver webDriver, URI uri){
        webDriver.get(uri.toString());
        return webDriver.getPageSource();
    }

    @Override
    public void destroy() {
        chromeDriverService.stop();
    }
}
