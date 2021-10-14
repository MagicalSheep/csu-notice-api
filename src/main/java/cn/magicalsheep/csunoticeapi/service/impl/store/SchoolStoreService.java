package cn.magicalsheep.csunoticeapi.service.impl.store;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.repository.content.SchoolNoticeContentRepository;
import cn.magicalsheep.csunoticeapi.repository.notice.SchoolNoticeRepository;
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
