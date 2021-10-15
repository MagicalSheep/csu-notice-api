package cn.magicalsheep.csunoticeapi.cse;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeContentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CseNoticeContentRepository
        extends JpaRepository<CSENoticeContent, Integer>, NoticeContentRepository {
}
