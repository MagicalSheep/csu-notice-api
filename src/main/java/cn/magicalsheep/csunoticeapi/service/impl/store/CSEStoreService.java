package cn.magicalsheep.csunoticeapi.service.impl.store;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.repository.content.CseNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.repository.notice.CseNoticeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CSEStoreService extends BaseStoreService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolStoreService.class);

    public CSEStoreService(
            CseNoticeRepository cseNoticeRepository,
            CseNoticeContentRepository cseNoticeContentRepository) {
        super(cseNoticeRepository, cseNoticeContentRepository, logger, NoticeType.CSE);
    }
}
