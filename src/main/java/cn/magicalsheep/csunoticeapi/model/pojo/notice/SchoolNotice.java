package cn.magicalsheep.csunoticeapi.model.pojo.notice;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "notice",
        indexes = {@Index(name = "notice_uri_index", columnList = "uri"),
                @Index(name = "notice_title_index", columnList = "title")})
public class SchoolNotice extends Notice {

}
