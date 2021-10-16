package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "headmaster_mail_content",
        indexes = {
                @Index(name = "headmaster_mail_content_uri_index", columnList = "uri")})
public class MailNoticeContent extends NoticeContent {
}
