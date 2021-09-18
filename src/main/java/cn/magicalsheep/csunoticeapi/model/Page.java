package cn.magicalsheep.csunoticeapi.model;

public class Page {

    private String content;
    private TYPE type;

    public enum TYPE {
        SCHOOL, CSE
    }

    public Page(TYPE type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public TYPE getType() {
        return type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(TYPE type) {
        this.type = type;
    }

}
