package cn.magicalsheep.csunoticeapi.model.response;

import cn.magicalsheep.csunoticeapi.model.entity.Notice;

public class NoticeResponse extends Response {

    private Notice notice;

    public NoticeResponse(Notice notice) {
        super();
        this.notice = notice;
    }

    public Notice getNotice() {
        return notice;
    }

    public void setNotice(Notice notice) {
        this.notice = notice;
    }
}
