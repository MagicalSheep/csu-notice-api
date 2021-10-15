package cn.magicalsheep.csunoticeapi.cse;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CseNoticeRepository
        extends JpaRepository<CSENotice, Integer>, NoticeRepository {
}
