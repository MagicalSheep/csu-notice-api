package cn.magicalsheep.csunoticeapi.model;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;

public class Page {

    private String content;
    private NoticeType type;

    public Page(NoticeType type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public NoticeType getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(NoticeType type) {
        this.type = type;
    }

}
