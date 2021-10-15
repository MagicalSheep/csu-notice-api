package cn.magicalsheep.csunoticeapi.school;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeContentRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolNoticeContentRepository
        extends JpaRepository<SchoolNoticeContent, Integer>, NoticeContentRepository {
}
