package cn.magicalsheep.csunoticeapi.service.impl.store;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import cn.magicalsheep.csunoticeapi.repository.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.repository.Repository;
import cn.magicalsheep.csunoticeapi.repository.SchoolNoticeRepository;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Optional;

public class BaseStoreService implements StoreService {

    private final Repository repository;

    private final Logger logger;
    private final NoticeType type;
    private static final int PageNoticeNum = 20;
    private int HEAD;

    public BaseStoreService(Repository repository, Logger logger, NoticeType type) {
        this.repository = repository;
        this.logger = logger;
        this.type = type;
    }

    private void save(Notice notice) {
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeRepository) repository).save((SchoolNotice) notice);
        else
            ((CseNoticeRepository) repository).save((CSENotice) notice);
    }

    private void flush() {
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeRepository) repository).flush();
        else
            ((CseNoticeRepository) repository).flush();
    }

    @Override
    public boolean isNeedToGetContent(Notice notice) {
        return !repository.existsByUri(notice.getUri()) ||
                repository.existsByUriAndContentIsNull(notice.getUri());
    }

    @Override
    public int save(ArrayList<Notice> notices) {
        for (int i = notices.size() - 1; i >= 0; i--) {
            Notice notice = notices.get(i);
            if (!repository.existsByUri(notice.getUri())) {
                save(notice);
            }
        }
        flush();
        HEAD = getLatestNotice().getId();
        logger.info("Update notices completed");
        logger.info("Current " + NoticeType.CSE + " head pointer is " + HEAD);
        return HEAD;
    }

    @Override
    public Notice getNoticeById(int id) {
        Optional<Notice> notice = repository.findNoticeById(id);
        if (notice.isEmpty())
            throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    @Override
    public Notice getLatestNotice() {
        Optional<Notice> notice = repository.findFirstByOrderByIdDesc();
        if (notice.isEmpty())
            throw new NullPointerException("Internal server error");
        return notice.get();
    }

    @Override
    public ArrayList<Notice> getNotices(int pageNum) {
        int ed = HEAD - PageNoticeNum * (pageNum - 1);
        int st = HEAD - PageNoticeNum * pageNum + 1;
        if (st <= 0 || ed <= 0)
            throw new NullPointerException("Invalid page number");
        return new ArrayList<>(repository.findAllByIdBetween(st, ed));
    }

    @Override
    public ArrayList<Notice> getDeltaNotices(int head) {
        if (head == HEAD)
            throw new NullPointerException("Nothing new to fetch");
        if (head > HEAD || head < 0)
            throw new NullPointerException("Invalid head id");
        return new ArrayList<>(repository.findAllByIdIsGreaterThan(head));
    }

    @Override
    public ArrayList<Notice> getNoticeByTitle(String title) {
        return new ArrayList<>(repository.findAllByTitleContains(title));
    }
}
