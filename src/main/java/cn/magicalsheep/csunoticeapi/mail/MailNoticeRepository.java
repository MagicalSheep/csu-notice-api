package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailNoticeRepository
        extends JpaRepository<MailNotice, Integer>, NoticeRepository {
}
