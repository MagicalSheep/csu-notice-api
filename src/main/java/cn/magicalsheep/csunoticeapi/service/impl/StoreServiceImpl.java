package cn.magicalsheep.csunoticeapi.service.impl;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.handler.HttpHandler;
import cn.magicalsheep.csunoticeapi.handler.http.CSEHttpHandler;
import cn.magicalsheep.csunoticeapi.handler.http.SchoolHttpHandler;
import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import cn.magicalsheep.csunoticeapi.util.IOUtils;
import cn.magicalsheep.csunoticeapi.repository.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.repository.SchoolNoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreServiceImpl implements StoreService {

    // repository begin
    private final SchoolNoticeRepository schoolNoticeRepository;
    private final CseNoticeRepository cseNoticeRepository;
    // repository end

    private final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);
    private static final int PageNoticeNum = 20;
    private final int[] HEAD = new int[10];

    public StoreServiceImpl(SchoolNoticeRepository schoolNoticeRepository, CseNoticeRepository cseNoticeRepository) {
        this.schoolNoticeRepository = schoolNoticeRepository;
        this.cseNoticeRepository = cseNoticeRepository;
    }

    private HttpHandler getHttpHandler(NoticeType type) {
        switch (type) {
            case CSE -> {
                return new CSEHttpHandler();
            }
            default -> {
                return new SchoolHttpHandler();
            }
        }
    }

    private void save(ArrayList<Notice> notices, NoticeType type) {
        if (type == NoticeType.SCHOOL) {
            for (int i = notices.size() - 1; i >= 0; i--) {
                Notice notice = notices.get(i);
                Optional<Notice> storeNotice = schoolNoticeRepository.findNoticeByUri(notice.getUri());
                if (storeNotice.isEmpty())
                    schoolNoticeRepository.save((SchoolNotice) notice);
            }
            schoolNoticeRepository.flush();
        } else if (type == NoticeType.CSE) {
            for (int i = notices.size() - 1; i >= 0; i--) {
                Notice notice = notices.get(i);
                Optional<Notice> storeNotice = cseNoticeRepository.findNoticeByUri(notice.getUri());
                if (storeNotice.isEmpty())
                    cseNoticeRepository.save((CSENotice) notice);
            }
            schoolNoticeRepository.flush();
        }
        HEAD[type.ordinal()] = getLatestNotice(type).getId();
        logger.info("Update notices completed");
        logger.info("Current " + type + " head pointer is " + HEAD[type.ordinal()]);
    }

    private void update(NoticeType type, int updatePageNum) throws Exception {
        logger.info("Start updating notices " + "(Type: " + type + ")");
        ArrayList<Notice> result = new ArrayList<>();
        try {
            for (int pageNum = 1; pageNum <= updatePageNum; pageNum++) {
                logger.info("Updating page " + pageNum + " (Type: " + type + ")");
                result.addAll(getHttpHandler(type).getNotices(pageNum));
            }
        } catch (PageEmptyException ignored) {
        }
        save(result, type);
    }

    public int getHEAD(NoticeType type) {
        return HEAD[type.ordinal()];
    }

    public void updateAll() throws Exception {
        logger.info("Updating the whole database, please waiting...");
        update(0x3f3f3f3f);
        logger.info("Update completed");
        Factory.getConfiguration().setInit_db(false);
        logger.info("Setting field init_db has been changed to false");
        IOUtils.saveConf(Factory.getConfiguration());
    }

    public void update(int updatePageNum) {
        if (Factory.getConfiguration().isSchool()) {
            new Thread(() -> {
                try {
                    update(NoticeType.SCHOOL, updatePageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Factory.getConfiguration().isCse()) {
            new Thread(() -> {
                try {
                    update(NoticeType.CSE, updatePageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
    }

    public Notice getNoticeById(NoticeType type, int id) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == NoticeType.SCHOOL)
            notice = schoolNoticeRepository.findNoticeById(id);
        else if (type == NoticeType.CSE)
            notice = cseNoticeRepository.findNoticeById(id);
        if (notice.isEmpty()) throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    public Notice getLatestNotice(NoticeType type) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == NoticeType.SCHOOL)
            notice = schoolNoticeRepository.findFirstByOrderByIdDesc();
        else if (type == NoticeType.CSE)
            notice = cseNoticeRepository.findFirstByOrderByIdDesc();
        if (notice.isEmpty()) throw new NullPointerException("Internal server error");
        return notice.get();
    }

    public ArrayList<Notice> getNotices(NoticeType type, int pageNum) throws NullPointerException {
        int ed = HEAD[type.ordinal()] - PageNoticeNum * (pageNum - 1);
        int st = HEAD[type.ordinal()] - PageNoticeNum * pageNum + 1;
        if (st <= 0 || ed <= 0) throw new NullPointerException("Invalid page number");
        if (type == NoticeType.SCHOOL)
            return new ArrayList<>(schoolNoticeRepository.findAllByIdBetween(st, ed));
        else if (type == NoticeType.CSE)
            return new ArrayList<>(cseNoticeRepository.findAllByIdBetween(st, ed));
        else
            return null;
    }

    public ArrayList<Notice> getDeltaNotices(NoticeType type, int head) throws NullPointerException {
        if (head == HEAD[type.ordinal()]) throw new NullPointerException("Nothing new to fetch");
        if (head > HEAD[type.ordinal()] || head < 0) throw new NullPointerException("Invalid head id");
        if (type == NoticeType.SCHOOL)
            return new ArrayList<>(schoolNoticeRepository.findAllByIdIsGreaterThan(head));
        else if (type == NoticeType.CSE)
            return new ArrayList<>(cseNoticeRepository.findAllByIdIsGreaterThan(head));
        else
            return null;
    }
}
