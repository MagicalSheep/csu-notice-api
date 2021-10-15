package cn.magicalsheep.csunoticeapi.school;

import cn.magicalsheep.csunoticeapi.common.service.impl.BaseStoreService;
import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SchoolStoreService extends BaseStoreService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolStoreService.class);

    public SchoolStoreService(
            SchoolNoticeRepository schoolNoticeRepository,
            SchoolNoticeContentRepository schoolNoticeContentRepository) {
        super(schoolNoticeRepository, schoolNoticeContentRepository, logger, NoticeType.SCHOOL);
    }
}
