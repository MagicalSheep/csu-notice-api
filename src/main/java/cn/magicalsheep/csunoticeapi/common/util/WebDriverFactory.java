package cn.magicalsheep.csunoticeapi.common.util;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class WebDriverFactory extends BasePooledObjectFactory<WebDriver> {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverFactory.class);

    private static final ChromeDriverService chromeDriverService;
    private static final ChromeOptions chromeOptions = new ChromeOptions();

    static {
        chromeOptions.setBinary(Factory.getConfiguration().getChrome_path());
        chromeOptions.addArguments("--no-sandbox");
        chromeOptions.addArguments("--headless");
        chromeOptions.addArguments("--window-size=1920,1080");
        chromeOptions.addArguments("--disable-gpu");
        chromeOptions.addArguments("--start-maximized");
        chromeOptions.addArguments("--user-agent=\"CSU Notice API Bot\"");
        chromeDriverService = new ChromeDriverService.Builder()
                .usingAnyFreePort()
                .build();
        try {
            chromeDriverService.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public WebDriver create() {
        return new RemoteWebDriver(chromeDriverService.getUrl(), chromeOptions);
    }

    @Override
    public PooledObject<WebDriver> wrap(WebDriver webDriver) {
        return new DefaultPooledObject<>(webDriver);
    }

    @Override
    public void passivateObject(PooledObject<WebDriver> webDriverPooledObject) {
        WebDriver driver = webDriverPooledObject.getObject();
        for (int tabs = driver.getWindowHandles().size(); tabs > 1; tabs--)
            driver.close();
    }

    @Override
    public void destroyObject(PooledObject<WebDriver> webDriverPooledObject) {
        webDriverPooledObject.getObject().quit();
    }

    public void destroy() {
        chromeDriverService.stop();
    }
}
