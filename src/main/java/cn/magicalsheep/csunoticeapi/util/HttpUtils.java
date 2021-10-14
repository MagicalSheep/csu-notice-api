package cn.magicalsheep.csunoticeapi.util;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.WebDriverFactory;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.model.packet.Packet;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
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
import java.time.Duration;
import java.util.Base64;

@Component
public class HttpUtils implements DisposableBean {

    private static final Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    private static final PooledObjectFactory<WebDriver> webDriverFactory
            = new WebDriverFactory();
    private static final GenericObjectPoolConfig<WebDriver> config
            = new GenericObjectPoolConfig<>();
    private static final ObjectPool<WebDriver> pool;
    private static final AShot ashot;

    static {
        config.setBlockWhenExhausted(true);
        config.setMaxWait(Duration.ofMillis(-1));
        config.setMaxTotal(15);
        config.setMinIdle(3);
        pool = new GenericObjectPool<>(webDriverFactory, config);

        ashot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000));
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
        try {
            WebDriver driver = pool.borrowObject();
            driver.get(uri);
            Thread.sleep(2000L);
            Screenshot screenshot = ashot.takeScreenshot(driver);
            pool.returnObject(driver);
            return imgToBase64String(screenshot.getImage());
        } catch (Exception e) {
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
     * @param uri uri
     * @return html
     */
    public static String getByBrowser(URI uri) {
        try {
            WebDriver driver = pool.borrowObject();
            driver.get(uri.toString());
            String ret = driver.getPageSource();
            pool.returnObject(driver);
            return ret;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return "";
        }
    }

    @Override
    public void destroy() {
        ((WebDriverFactory) webDriverFactory).destroy();
    }
}
