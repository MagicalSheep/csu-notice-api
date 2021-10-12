package cn.magicalsheep.csunoticeapi.service;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.util.ArrayList;

public interface StoreService {

    boolean isNeedToGetContent(NoticeType type, Notice notice);

    int save(ArrayList<Notice> notices, NoticeType type);

    Notice getNoticeById(NoticeType type, int id);

    Notice getLatestNotice(NoticeType type);

    ArrayList<Notice> getNotices(NoticeType type, int pageNum);

    ArrayList<Notice> getDeltaNotices(NoticeType type, int head);

    ArrayList<Notice> getNoticeByTitle(NoticeType type, String title);
}
