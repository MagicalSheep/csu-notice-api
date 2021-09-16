package cn.magicalsheep.csunoticeapi.store;

import cn.magicalsheep.csunoticeapi.model.Notice;

import java.util.ArrayList;

public class Repository {

    private int head = 0;

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public void update() {
//        Factory.getConnector().getNotices(num);
        // TODO: Update notices to database
    }

    public ArrayList<Notice> getNotices(int pageNum) {
        // TODO: Fetch notices from database
        return null;
    }

    public Notice getNoticeById(int id) {
        // TODO: JPA Query
        return null;
    }
}
