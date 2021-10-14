package cn.magicalsheep.csunoticeapi.service.impl.store;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.pojo.content.CSENoticeContent;
import cn.magicalsheep.csunoticeapi.model.pojo.content.NoticeContent;
import cn.magicalsheep.csunoticeapi.model.pojo.content.SchoolNoticeContent;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.CSENotice;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.Notice;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.SchoolNotice;
import cn.magicalsheep.csunoticeapi.repository.content.CseNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.repository.content.NoticeContentRepository;
import cn.magicalsheep.csunoticeapi.repository.content.SchoolNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.repository.notice.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.repository.notice.NoticeRepository;
import cn.magicalsheep.csunoticeapi.repository.notice.SchoolNoticeRepository;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;

public class BaseStoreService implements StoreService {

    private final NoticeRepository noticeRepository;
    private final NoticeContentRepository noticeContentRepository;

    private final Logger logger;
    private final NoticeType type;
    private static final int PageNoticeNum = 20;
    private int HEAD;

    public BaseStoreService(
            NoticeRepository noticeRepository, NoticeContentRepository noticeContentRepository,
            Logger logger, NoticeType type) {
        this.noticeRepository = noticeRepository;
        this.noticeContentRepository = noticeContentRepository;
        this.logger = logger;
        this.type = type;
    }

    public int save(Notice notice) {
        saveFunc(notice);
        flush();
        HEAD = getLatestNotice().getId();
        return HEAD;
    }

    @Override
    public void save(NoticeContent noticeContent) {
        saveFunc(noticeContent);
        flush();
    }

    private void saveFunc(NoticeContent noticeContent) {
        noticeContent.setUpdateTime(new Date());
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeContentRepository) noticeContentRepository)
                    .save((SchoolNoticeContent) noticeContent);
        else
            ((CseNoticeContentRepository) noticeContentRepository)
                    .save((CSENoticeContent) noticeContent);
    }

    private void saveFunc(Notice notice) {
        notice.setUpdateTime(new Date());
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeRepository) noticeRepository).save((SchoolNotice) notice);
        else
            ((CseNoticeRepository) noticeRepository).save((CSENotice) notice);
    }

    private void flush() {
        if (type == NoticeType.SCHOOL) {
            ((SchoolNoticeRepository) noticeRepository).flush();
            ((SchoolNoticeContentRepository) noticeContentRepository).flush();
        } else {
            ((CseNoticeRepository) noticeRepository).flush();
            ((CseNoticeContentRepository) noticeContentRepository).flush();
        }
    }

    @Override
    public boolean isNeedToGetContent(Notice notice) {
        if (!noticeRepository.existsByUri(notice.getUri()))
            return true;
        return !noticeContentRepository.existsByUriAndContentNotNull(notice.getUri());
    }

    @Override
    public int save(ArrayList<Notice> notices) {
        for (int i = notices.size() - 1; i >= 0; i--) {
            Notice notice = notices.get(i);
            if (!noticeRepository.existsByUri(notice.getUri())) {
                saveFunc(notice);
            }
        }
        flush();
        HEAD = getLatestNotice().getId();
        return HEAD;
    }

    @Override
    public Notice getNoticeById(int id) {
        Optional<Notice> notice = noticeRepository.findNoticeById(id);
        if (notice.isEmpty())
            throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    @Override
    public Notice getLatestNotice() {
        Optional<Notice> notice = noticeRepository.findFirstByOrderByIdDesc();
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
        return new ArrayList<>(noticeRepository.findAllByIdBetween(st, ed));
    }

    @Override
    public ArrayList<Notice> getDeltaNotices(int head) {
        if (head == HEAD)
            throw new NullPointerException("Nothing new to fetch");
        if (head > HEAD || head < 0)
            throw new NullPointerException("Invalid head id");
        return new ArrayList<>(noticeRepository.findAllByIdIsGreaterThan(head));
    }

    @Override
    public ArrayList<Notice> getNoticeByTitle(String title) {
        return new ArrayList<>(noticeRepository.findAllByTitleContains(title));
    }

    @Override
    public NoticeContent getNoticeContentByNoticeId(int noticeId) {
        Optional<Notice> res = noticeRepository.findNoticeById(noticeId);
        if (res.isEmpty())
            return null;
        Optional<NoticeContent> content = noticeContentRepository.
                findNoticeContentByUri(res.get().getUri());
        if (content.isEmpty())
            return null;
        return content.get();
    }
}
