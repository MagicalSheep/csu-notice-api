package cn.magicalsheep.csunoticeapi.service.impl;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import cn.magicalsheep.csunoticeapi.repository.CseNoticeRepository;
import cn.magicalsheep.csunoticeapi.repository.SchoolNoticeRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StoreServiceImpl implements StoreService {

    // repository begin
    private final SchoolNoticeRepository schoolNoticeRepository;
    private final CseNoticeRepository cseNoticeRepository;
    // repository end

    private final Logger logger = LoggerFactory.getLogger(StoreServiceImpl.class);
    private static final int PageNoticeNum = 20;
    private final int[] HEAD = new int[10];

    @Override
    public boolean isNeedToGetContent(NoticeType type, Notice notice) {
        if (type == NoticeType.SCHOOL)
            return !schoolNoticeRepository.existsByUri(notice.getUri()) ||
                    schoolNoticeRepository.existsByUriAndContentIsNull(notice.getUri());
        else
            return !cseNoticeRepository.existsByUri(notice.getUri()) ||
                    cseNoticeRepository.existsByUriAndContentIsNull(notice.getUri());
    }

    @Override
    public int save(ArrayList<Notice> notices, NoticeType type) {
        if (type == NoticeType.SCHOOL) {
            for (int i = notices.size() - 1; i >= 0; i--) {
                Notice notice = notices.get(i);
                if (!schoolNoticeRepository.existsByUri(notice.getUri()))
                    schoolNoticeRepository.save((SchoolNotice) notice);
            }
            schoolNoticeRepository.flush();
        } else if (type == NoticeType.CSE) {
            for (int i = notices.size() - 1; i >= 0; i--) {
                Notice notice = notices.get(i);
                if (!cseNoticeRepository.existsByUri(notice.getUri()))
                    cseNoticeRepository.save((CSENotice) notice);
            }
            schoolNoticeRepository.flush();
        }
        int head = getLatestNotice(type).getId();
        HEAD[type.ordinal()] = head;
        logger.info("Update notices completed");
        logger.info("Current " + type + " head pointer is " + HEAD[type.ordinal()]);
        return head;
    }

    @Override
    public Notice getNoticeById(NoticeType type, int id) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == NoticeType.SCHOOL)
            notice = schoolNoticeRepository.findNoticeById(id);
        else if (type == NoticeType.CSE)
            notice = cseNoticeRepository.findNoticeById(id);
        if (notice.isEmpty()) throw new NullPointerException("Invalid notice");
        return notice.get();
    }

    @Override
    public Notice getLatestNotice(NoticeType type) throws NullPointerException {
        Optional<Notice> notice = Optional.empty();
        if (type == NoticeType.SCHOOL)
            notice = schoolNoticeRepository.findFirstByOrderByIdDesc();
        else if (type == NoticeType.CSE)
            notice = cseNoticeRepository.findFirstByOrderByIdDesc();
        if (notice.isEmpty()) throw new NullPointerException("Internal server error");
        return notice.get();
    }

    @Override
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

    @Override
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
