package cn.magicalsheep.csunoticeapi.common.util;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public class BrowserContextFactory extends BasePooledObjectFactory<BrowserContext> {

    private static final Logger logger = LoggerFactory.getLogger(BrowserContextFactory.class);
    private static final HashMap<BrowserContext, Playwright> playwrights
            = new HashMap<>();

    @Override
    public BrowserContext create() {
        Playwright playwright = Playwright.create();
        BrowserContext browserContext = playwright.chromium().launch().newContext();
        playwrights.put(browserContext, playwright);
        return browserContext;
    }

    @Override
    public PooledObject<BrowserContext> wrap(BrowserContext browserContext) {
        return new DefaultPooledObject<>(browserContext);
    }

    @Override
    public void passivateObject(PooledObject<BrowserContext> browserContextPooledObject) {
        List<Page> pageList = browserContextPooledObject.getObject().pages();
        for (Page page : pageList)
            page.close();
    }

    @Override
    public void destroyObject(PooledObject<BrowserContext> browserContextPooledObject) {
        Playwright playwright = playwrights.get(browserContextPooledObject.getObject());
        assert playwright != null;
        playwrights.remove(browserContextPooledObject.getObject());
        playwright.close();
    }

    public void destroy() {
        playwrights.forEach((browserContext, playwright) -> playwright.close());
    }
}
