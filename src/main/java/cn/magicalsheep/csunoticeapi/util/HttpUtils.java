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
    private static final WebDriver driver;
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
        Screenshot screenshot = ashot.takeScreenshot(driver);
        return imgToBase64String(screenshot.getImage());
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
