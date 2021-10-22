package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.service.impl.BaseStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class XgwStoreService extends BaseStoreService {

    private static final Logger logger = LoggerFactory.getLogger(XgwStoreService.class);

    public XgwStoreService(
            XgwNoticeRepository noticeRepository,
            XgwNoticeContentRepository noticeContentRepository) {
        super(noticeRepository, noticeContentRepository, logger, NoticeType.XGW);
    }
}
