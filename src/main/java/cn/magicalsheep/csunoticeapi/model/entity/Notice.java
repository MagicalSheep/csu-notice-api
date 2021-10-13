package cn.magicalsheep.csunoticeapi.model.entity;

import javax.persistence.*;

@MappedSuperclass
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Lob
    private String title;

    private String from;

    private String uri;

    @Lob
    private String content;

    public Notice() {
    }

    public Notice(int id, String title, String from, String uri, String content) {
        this.id = id;
        this.title = title;
        this.from = from;
        this.uri = uri;
        this.content = content;
    }

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
