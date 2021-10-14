package cn.magicalsheep.csunoticeapi.repository.content;

import cn.magicalsheep.csunoticeapi.model.pojo.content.SchoolNoticeContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolNoticeContentRepository
        extends JpaRepository<SchoolNoticeContent, Integer>, NoticeContentRepository {
}
