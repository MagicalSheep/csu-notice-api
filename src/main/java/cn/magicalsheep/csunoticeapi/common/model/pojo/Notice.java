package cn.magicalsheep.csunoticeapi.common.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@AllArgsConstructor
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
