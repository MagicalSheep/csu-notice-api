package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "headmaster_mail",
        indexes = {@Index(name = "headmaster_mail_uri_index", columnList = "uri"),
                @Index(name = "headmaster_mail_title_index", columnList = "title")})
public class MailNotice extends Notice {
}
