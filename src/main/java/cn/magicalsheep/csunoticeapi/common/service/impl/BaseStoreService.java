package cn.magicalsheep.csunoticeapi.common.service.impl;

import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.cse.CSENoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.mail.MailNotice;
import cn.magicalsheep.csunoticeapi.mail.MailNoticeContent;
import cn.magicalsheep.csunoticeapi.mail.MailNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.mail.MailNoticeRepository;
import cn.magicalsheep.csunoticeapi.school.SchoolNoticeContent;
import cn.magicalsheep.csunoticeapi.cse.CSENotice;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.school.SchoolNotice;
import cn.magicalsheep.csunoticeapi.cse.CseNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.common.repository.NoticeContentRepository;
import cn.magicalsheep.csunoticeapi.school.SchoolNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.cse.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.common.repository.NoticeRepository;
import cn.magicalsheep.csunoticeapi.school.SchoolNoticeRepository;
import cn.magicalsheep.csunoticeapi.common.service.StoreService;
import cn.magicalsheep.csunoticeapi.xgw.XgwNotice;
import cn.magicalsheep.csunoticeapi.xgw.XgwNoticeContent;
import cn.magicalsheep.csunoticeapi.xgw.XgwNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.xgw.XgwNoticeRepository;
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
        Optional<NoticeContent> res =
                noticeContentRepository.findNoticeContentByUri(noticeContent.getUri());
        if (res.isEmpty()) {
            saveFunc(noticeContent);
        } else {
            res.get().setContent(noticeContent.getContent());
            saveFunc(res.get());
        }
        flush();
    }

    private void saveFunc(NoticeContent noticeContent) {
        noticeContent.setUpdateTime(new Date());
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeContentRepository) noticeContentRepository)
                    .save((SchoolNoticeContent) noticeContent);
        else if (type == NoticeType.CSE)
            ((CseNoticeContentRepository) noticeContentRepository)
                    .save((CSENoticeContent) noticeContent);
        else if (type == NoticeType.MAIL)
            ((MailNoticeContentRepository) noticeContentRepository)
                    .save((MailNoticeContent) noticeContent);
        else
            ((XgwNoticeContentRepository) noticeContentRepository)
                    .save((XgwNoticeContent) noticeContent);
    }

    private void saveFunc(Notice notice) {
        notice.setUpdateTime(new Date());
        if (type == NoticeType.SCHOOL)
            ((SchoolNoticeRepository) noticeRepository).save((SchoolNotice) notice);
        else if (type == NoticeType.CSE)
            ((CseNoticeRepository) noticeRepository).save((CSENotice) notice);
        else if (type == NoticeType.MAIL)
            ((MailNoticeRepository) noticeRepository).save((MailNotice) notice);
        else
            ((XgwNoticeRepository) noticeRepository).save((XgwNotice) notice);
    }

    private void flush() {
        if (type == NoticeType.SCHOOL) {
            ((SchoolNoticeRepository) noticeRepository).flush();
            ((SchoolNoticeContentRepository) noticeContentRepository).flush();
        } else if (type == NoticeType.CSE) {
            ((CseNoticeRepository) noticeRepository).flush();
            ((CseNoticeContentRepository) noticeContentRepository).flush();
        } else if (type == NoticeType.MAIL) {
            ((MailNoticeRepository) noticeRepository).flush();
            ((MailNoticeContentRepository) noticeContentRepository).flush();
        } else {
            ((XgwNoticeRepository) noticeRepository).flush();
            ((XgwNoticeContentRepository) noticeContentRepository).flush();
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
