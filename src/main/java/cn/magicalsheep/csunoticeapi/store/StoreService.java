package cn.magicalsheep.csunoticeapi.store;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.model.Notice;
import cn.magicalsheep.csunoticeapi.store.repository.NoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class StoreService {

    private final Logger logger = LoggerFactory.getLogger(StoreService.class);
    private final NoticeRepository noticeRepository;
    private static final int PageNoticeNum = 20;

    private int HEAD = 0;

    public StoreService(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public int getHEAD() {
        return HEAD;
    }

    public void updateAll() throws Exception {
        logger.info("Updating the whole database, please waiting...");
        update(0x3f3f3f3f);
        logger.info("Update completed");
        Factory.getConfiguration().setInit_db(false);
        logger.info("Setting field init_db has been changed to false");
        IOHandler.saveConf(Factory.getConfiguration());
    }

    public void update(int updatePageNum) throws Exception {
        ArrayList<Notice> result = new ArrayList<>();
        int pageNum = 1;
        try {
            for (int i = 1; i <= updatePageNum; i++) {
                logger.info("Updating page " + pageNum);
                result.addAll(Factory.getConnector().getNotices(pageNum++));
            }
        } catch (PageEmptyException ignored) {
        }
        for (int i = result.size() - 1; i >= 0; i--) {
            Notice notice = result.get(i);
            Optional<Notice> storeNotice = noticeRepository.findNoticeByUri(notice.getUri());
            if (storeNotice.isEmpty())
                noticeRepository.save(notice);
        }
        noticeRepository.flush();
        HEAD = getLatestNotice().getId();
        logger.info("Current head pointer is " + HEAD);
    }

    public Notice getNoticeById(int id) throws NullPointerException {
        Optional<Notice> notice = noticeRepository.findNoticeById(id);
        if (notice.isEmpty()) throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    public Notice getLatestNotice() throws NullPointerException {
        Optional<Notice> notice = noticeRepository.findFirstByOrderByIdDesc();
        if (notice.isEmpty()) throw new NullPointerException("Internal server error");
        return notice.get();
    }

    public ArrayList<Notice> getNotices(int pageNum) throws NullPointerException {
        int ed = HEAD - PageNoticeNum * (pageNum - 1);
        int st = HEAD - PageNoticeNum * pageNum + 1;
        if (st <= 0 || ed <= 0) throw new NullPointerException("Invalid page number");
        return new ArrayList<>(noticeRepository.findAllByIdBetween(st, ed));
    }

    public ArrayList<Notice> getDeltaNotices(int head) throws NullPointerException {
        if (head == HEAD) throw new NullPointerException("Nothing new to fetch");
        if (head > HEAD || head < 0) throw new NullPointerException("Invalid head id");
        return new ArrayList<>(noticeRepository.findAllByIdIsGreaterThan(head));
    }
}
