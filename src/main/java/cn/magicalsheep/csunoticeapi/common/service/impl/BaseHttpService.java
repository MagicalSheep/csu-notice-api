package cn.magicalsheep.csunoticeapi.common.service.impl;

import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.service.HttpService;
import cn.magicalsheep.csunoticeapi.common.service.StoreService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class BaseHttpService implements HttpService {

    private final Logger logger;
    private final NoticeType type;
    private final StoreService storeService;
    private final HashSet<String> fetchStatus;
    private int HEAD;

    public BaseHttpService(StoreService storeService, Logger logger, NoticeType type) {
        this.type = type;
        this.logger = logger;
        this.storeService = storeService;
        fetchStatus = new HashSet<>();
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
            new Thread(() -> loadContent(res)).start();
        }
        logger.info("Update notices completed");
        logger.info("Current " + type + " head pointer is " + HEAD);
    }

    @Override
    public abstract void updateAll() throws Exception;

    @Override
    public void loadContent(Notice notice) {
        loadContent(notice, false);
    }

    @Override
    public void loadContent(Notice notice, boolean isForce) {
        if (fetchStatus.contains(notice.getUri()))
            return;
        if (!isForce && !storeService.isNeedToGetContent(notice))
            return;
        safeAddStatus(notice.getUri());
        new Thread(() -> {
            storeService.save(fetchContent(notice));
            safeRemoveStatus(notice.getUri());
        }).start();
    }

    @Override
    public void loadContent(ArrayList<Notice> notices) {
        for (Notice notice : notices)
            loadContent(notice);
    }

    protected abstract ArrayList<Notice> getNotices(int pageNum) throws Exception;

    protected abstract NoticeContent fetchContent(Notice notice);

    protected abstract ArrayList<Notice> parse(String html) throws Exception;

    private synchronized void safeAddStatus(String uri) {
        fetchStatus.add(uri);
    }

    private synchronized void safeRemoveStatus(String uri) {
        fetchStatus.remove(uri);
    }
}
