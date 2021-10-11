package cn.magicalsheep.csunoticeapi.service;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.util.ArrayList;

public interface StoreService {

    int getHEAD(NoticeType type);

    void updateAll() throws Exception;

    void update(int updatePageNum);

    Notice getNoticeById(NoticeType type, int id);

    Notice getLatestNotice(NoticeType type);

    ArrayList<Notice> getNotices(NoticeType type, int pageNum);

    ArrayList<Notice> getDeltaNotices(NoticeType type, int head);

}
