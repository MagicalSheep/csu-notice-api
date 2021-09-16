package cn.magicalsheep.csunoticeapi.model;

public class Notice {

    private int id;
    private String title;
    private String from;
    private String uri;
    private String content;

    public int getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getFrom() {
        return from;
    }

    public String getTitle() {
        return title;
    }

    public String getUri() {
        return uri;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
