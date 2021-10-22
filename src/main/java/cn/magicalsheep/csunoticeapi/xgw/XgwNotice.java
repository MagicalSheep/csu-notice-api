package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "xgw_notice",
        indexes = {@Index(name = "xgw_notice_uri_index", columnList = "uri"),
                @Index(name = "xgw_notice_title_index", columnList = "title")})
public class XgwNotice extends Notice {
}
