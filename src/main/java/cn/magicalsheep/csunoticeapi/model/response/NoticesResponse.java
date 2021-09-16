package cn.magicalsheep.csunoticeapi.model.response;

import cn.magicalsheep.csunoticeapi.model.Notice;

import java.util.ArrayList;

public class NoticesResponse extends Response {

    private ArrayList<Notice> notices;

    public NoticesResponse(ArrayList<Notice> notices) {
        super();
        this.notices = notices;
    }

    public ArrayList<Notice> getNotices() {
        return notices;
    }

    public void setNotices(ArrayList<Notice> notices) {
        this.notices = notices;
    }
}
