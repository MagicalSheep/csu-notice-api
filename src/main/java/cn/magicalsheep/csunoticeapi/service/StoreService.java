package cn.magicalsheep.csunoticeapi.service;

import cn.magicalsheep.csunoticeapi.model.pojo.content.NoticeContent;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.Notice;

import java.util.ArrayList;

public interface StoreService {

    boolean isNeedToGetContent(Notice notice);

    int save(ArrayList<Notice> notices);

    int save(Notice notice);

    void save(NoticeContent noticeContent);

    Notice getNoticeById(int id);

    Notice getLatestNotice();

    ArrayList<Notice> getNotices(int pageNum);

    ArrayList<Notice> getDeltaNotices(int head);

    ArrayList<Notice> getNoticeByTitle(String title);

    NoticeContent getNoticeContentByNoticeId(int noticeId);
}
