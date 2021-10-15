package cn.magicalsheep.csunoticeapi.cse;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "cse_notice",
        indexes = {@Index(name = "cse_notice_uri_index", columnList = "uri"),
                @Index(name = "cse_notice_title_index", columnList = "title")})
public class CSENotice extends Notice {
}
