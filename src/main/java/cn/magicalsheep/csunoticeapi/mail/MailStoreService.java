package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.service.impl.BaseStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MailStoreService extends BaseStoreService {

    private static final Logger logger = LoggerFactory.getLogger(MailStoreService.class);

    public MailStoreService(
            MailNoticeRepository mailNoticeRepository,
            MailNoticeContentRepository mailNoticeContentRepository) {
        super(mailNoticeRepository, mailNoticeContentRepository, logger, NoticeType.MAIL);
    }
}
