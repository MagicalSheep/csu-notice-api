package cn.magicalsheep.csunoticeapi.store;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.http.connector.Connector;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import cn.magicalsheep.csunoticeapi.store.repository.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.store.repository.SchoolNoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreService {

    // repository begin
    private final SchoolNoticeRepository schoolNoticeRepository;
    private final CseNoticeRepository cseNoticeRepository;
    // repository end

    private final Logger logger = LoggerFactory.getLogger(StoreService.class);
    private static final int PageNoticeNum = 20;
    private final int[] HEAD = new int[10];

    public StoreService(SchoolNoticeRepository schoolNoticeRepository, CseNoticeRepository cseNoticeRepository) {
        this.schoolNoticeRepository = schoolNoticeRepository;
        this.cseNoticeRepository = cseNoticeRepository;
    }

    private void save(ArrayList<Notice> notices, Page.TYPE type) {
        if (type == Page.TYPE.SCHOOL) {
            for (int i = notices.size() - 1; i >= 0; i--) {
                Notice notice = notices.get(i);
                Optional<Notice> storeNotice = schoolNoticeRepository.findNoticeByUri(notice.getUri());
                if (storeNotice.isEmpty())
                    schoolNoticeRepository.save((SchoolNotice) notice);
            }
            schoolNoticeRepository.flush();
        } else if (type == Page.TYPE.CSE) {
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

    private void update(Page.TYPE type, int updatePageNum) throws Exception {
        Connector connector = Factory.getConnector(type);
        logger.info("Start updating notices " + "(Type: " + type + ")");
        ArrayList<Notice> result = new ArrayList<>();
        try {
            for (int pageNum = 1; pageNum <= updatePageNum; pageNum++) {
                logger.info("Updating page " + pageNum + " (Type: " + type + ")");
                result.addAll(connector.getNotices(pageNum++));
            }
        } catch (PageEmptyException ignored) {
        }
        save(result, type);
    }

    public int getHEAD(Page.TYPE type) {
        return HEAD[type.ordinal()];
    }

    public void updateAll() throws Exception {
        logger.info("Updating the whole database, please waiting...");
        update(0x3f3f3f3f);
        logger.info("Update completed");
        Factory.getConfiguration().setInit_db(false);
        logger.info("Setting field init_db has been changed to false");
        IOHandler.saveConf(Factory.getConfiguration());
    }

    public void update(int updatePageNum) {
        if (Factory.getConfiguration().isSchool()) {
            new Thread(() -> {
                try {
                    update(Page.TYPE.SCHOOL, updatePageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Factory.getConfiguration().isCse()) {
            new Thread(() -> {
                try {
                    update(Page.TYPE.CSE, updatePageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
    }

    public Notice getNoticeById(Page.TYPE type, int id) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == Page.TYPE.SCHOOL)
            notice = schoolNoticeRepository.findNoticeById(id);
        else if (type == Page.TYPE.CSE)
            notice = cseNoticeRepository.findNoticeById(id);
        if (notice.isEmpty()) throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    public Notice getLatestNotice(Page.TYPE type) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == Page.TYPE.SCHOOL)
            notice = schoolNoticeRepository.findFirstByOrderByIdDesc();
        else if (type == Page.TYPE.CSE)
            notice = cseNoticeRepository.findFirstByOrderByIdDesc();
        if (notice.isEmpty()) throw new NullPointerException("Internal server error");
        return notice.get();
    }

    public ArrayList<Notice> getNotices(Page.TYPE type, int pageNum) throws NullPointerException {
        int ed = HEAD[type.ordinal()] - PageNoticeNum * (pageNum - 1);
        int st = HEAD[type.ordinal()] - PageNoticeNum * pageNum + 1;
        if (st <= 0 || ed <= 0) throw new NullPointerException("Invalid page number");
        if (type == Page.TYPE.SCHOOL)
            return new ArrayList<>(schoolNoticeRepository.findAllByIdBetween(st, ed));
        else if (type == Page.TYPE.CSE)
            return new ArrayList<>(cseNoticeRepository.findAllByIdBetween(st, ed));
        throw new NullPointerException("Invalid page type: Internal server error");
    }

    public ArrayList<Notice> getDeltaNotices(Page.TYPE type, int head) throws NullPointerException {
        if (head == HEAD[type.ordinal()]) throw new NullPointerException("Nothing new to fetch");
        if (head > HEAD[type.ordinal()] || head < 0) throw new NullPointerException("Invalid head id");
        if (type == Page.TYPE.SCHOOL)
            return new ArrayList<>(schoolNoticeRepository.findAllByIdIsGreaterThan(head));
        else if (type == Page.TYPE.CSE)
            return new ArrayList<>(cseNoticeRepository.findAllByIdIsGreaterThan(head));
        throw new NullPointerException("Invalid page type: Internal server error");
    }
}
