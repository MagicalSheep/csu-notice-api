package cn.magicalsheep.csunoticeapi.common.service.impl;

import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.service.HttpService;
import cn.magicalsheep.csunoticeapi.common.service.StoreService;
import org.slf4j.Logger;

import java.util.ArrayList;

public abstract class BaseHttpService implements HttpService {

    private final Logger logger;
    private final NoticeType type;
    private final StoreService storeService;
    private int HEAD;

    public BaseHttpService(StoreService storeService, Logger logger, NoticeType type) {
        this.type = type;
        this.logger = logger;
        this.storeService = storeService;
    }

    @Override
    public int getHEAD() {
        return HEAD;
    }

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

    @Override
    public abstract void updateAll() throws Exception;

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
