package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface XgwNoticeRepository
        extends JpaRepository<XgwNotice, Integer>, NoticeRepository {
}
