package cn.magicalsheep.csunoticeapi.cse;

import cn.magicalsheep.csunoticeapi.common.service.impl.BaseStoreService;
import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.school.SchoolStoreService;
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
