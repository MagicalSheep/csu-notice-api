package cn.magicalsheep.csunoticeapi.service.impl.http;

import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.service.HttpService;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public abstract class BaseHttpService implements HttpService {

    protected final Logger logger = LoggerFactory.getLogger(BaseHttpService.class);
    protected final NoticeType type;
    protected final StoreService storeService;
    private int HEAD;

    public BaseHttpService(StoreService storeService, NoticeType type) {
        this.type = type;
        this.storeService = storeService;
    }

    @Override
    public int getHEAD() {
        return HEAD;
    }

    @Override
    public void update(int updatePageNum) throws Exception {
        logger.info("Start updating notices " + "(Type: " + type + ")");
        ArrayList<Notice> result = new ArrayList<>();
        try {
            for (int pageNum = 1; pageNum <= updatePageNum; pageNum++) {
                logger.info("Updating page " + pageNum + " (Type: " + type + ")");
                result.addAll(getNotices(pageNum));
            }
        } catch (PageEmptyException ignored) {
        }
        int i = 0, count = 0;
        logger.info("Updating notice content, please waiting...(Type: " + type + ")");
        for (Notice notice : result) {
            ++i;
            logger.info("Updating notice " + i + " content (Type: " + type + ")");
            if (storeService.isNeedToGetContent(type, notice)) {
                ++count;
                notice.setContent(fetchContent(notice));
            }
        }
        logger.info("Updated " + count + " notices content (Type: " + type + ")");
        HEAD = storeService.save(result, type);
    }

    protected abstract ArrayList<Notice> getNotices(int pageNum) throws Exception;

    protected abstract String fetchContent(Notice notice);
}
