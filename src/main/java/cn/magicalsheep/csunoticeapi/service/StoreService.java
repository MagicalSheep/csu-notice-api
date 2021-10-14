package cn.magicalsheep.csunoticeapi.service;

import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.util.ArrayList;

public interface StoreService {

    boolean isNeedToGetContent(Notice notice);

    int save(ArrayList<Notice> notices);

    Notice getNoticeById(int id);

    Notice getLatestNotice();

    ArrayList<Notice> getNotices(int pageNum);

    ArrayList<Notice> getDeltaNotices(int head);

    ArrayList<Notice> getNoticeByTitle(String title);
}
