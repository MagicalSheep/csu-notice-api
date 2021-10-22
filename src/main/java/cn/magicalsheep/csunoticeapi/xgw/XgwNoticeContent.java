package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "xgw_content",
        indexes = {
                @Index(name = "xgw_content_uri_index", columnList = "uri")})
public class XgwNoticeContent extends NoticeContent {
}
