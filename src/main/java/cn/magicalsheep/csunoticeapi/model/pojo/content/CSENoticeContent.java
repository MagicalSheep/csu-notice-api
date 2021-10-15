package cn.magicalsheep.csunoticeapi.model.pojo.content;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "cse_content",
        indexes = {
                @Index(name = "cse_content_uri_index", columnList = "uri")})
public class CSENoticeContent extends NoticeContent {
}
