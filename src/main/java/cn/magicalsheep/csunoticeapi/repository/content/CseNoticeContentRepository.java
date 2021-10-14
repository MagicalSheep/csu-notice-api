package cn.magicalsheep.csunoticeapi.repository.content;

import cn.magicalsheep.csunoticeapi.model.pojo.content.CSENoticeContent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CseNoticeContentRepository
        extends JpaRepository<CSENoticeContent, Integer>, NoticeContentRepository {
}
