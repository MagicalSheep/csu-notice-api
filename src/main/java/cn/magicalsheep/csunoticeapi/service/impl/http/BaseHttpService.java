package cn.magicalsheep.csunoticeapi.service.impl.http;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.pojo.content.NoticeContent;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.Notice;
import cn.magicalsheep.csunoticeapi.service.HttpService;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;

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

//    private void updateV1(int updatePageNum) throws Exception {
//        logger.info("Start updating notices " + "(Type: " + type + ")");
//        ArrayList<Notice> result = new ArrayList<>();
//        try {
//            for (int pageNum = 1; pageNum <= updatePageNum; pageNum++) {
//                logger.info("Updating page " + pageNum + " (Type: " + type + ")");
//                result.addAll(getNotices(pageNum));
//            }
//        } catch (PageEmptyException ignored) {
//        }
//        logger.info("Update notices completed");
//        logger.info("Current " + type + " head pointer is " + HEAD);
//        HEAD = storeService.save(result);
//    }

    @Override
    public void update(int updatePageNum) throws Exception {
        logger.info("Start updating notices " + "(Type: " + type + ")");
        for (int i = updatePageNum; i >= 1; i--) {
            logger.info("Updating page " + (updatePageNum - i + 1) + " (Type: " + type + ")");
            ArrayList<Notice> res = getNotices(i);
            HEAD = storeService.save(res);
            new Thread(() -> handleContent(res)).start();
        }
        logger.info("Update notices completed");
        logger.info("Current " + type + " head pointer is " + HEAD);
    }

    protected abstract ArrayList<Notice> getNotices(int pageNum) throws Exception;

    protected abstract NoticeContent fetchContent(Notice notice);

    private void handleContent(ArrayList<Notice> notices) {
        for (Notice notice : notices) {
            if (!storeService.isNeedToGetContent(notice))
                continue;
            new Thread(() -> storeService.save(fetchContent(notice))).start();
        }
    }
}
