package cn.magicalsheep.csunoticeapi.model.pojo.content;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "school_content",
        indexes = {
                @Index(name = "school_content_uri_index", columnList = "uri")})
public class SchoolNoticeContent extends NoticeContent {
}
