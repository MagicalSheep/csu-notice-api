package cn.magicalsheep.csunoticeapi.model.pojo.content;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@Table(indexes = {@Index(columnList = "uri")})
public class NoticeContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String uri;

    @Lob
    private String content;

    private Date createTime;

    private Date updateTime;

    public NoticeContent() {
        createTime = new Date();
    }
}
