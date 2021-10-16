package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeContentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailNoticeContentRepository
        extends JpaRepository<MailNoticeContent, Integer>, NoticeContentRepository {
}
