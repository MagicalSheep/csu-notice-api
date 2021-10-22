package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeContentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XgwNoticeContentRepository
        extends JpaRepository<XgwNoticeContent, Integer>, NoticeContentRepository {
}
