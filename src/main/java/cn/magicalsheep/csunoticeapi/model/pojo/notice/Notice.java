package cn.magicalsheep.csunoticeapi.model.pojo.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "uri")})
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String from;

    private String uri;

    private Date createTime;

    private Date updateTime;

    public Notice() {
        createTime = new Date();
    }
}
