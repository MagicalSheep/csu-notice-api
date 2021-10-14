package cn.magicalsheep.csunoticeapi.repository.notice;

import cn.magicalsheep.csunoticeapi.model.pojo.notice.CSENotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CseNoticeRepository
        extends JpaRepository<CSENotice, Integer>, NoticeRepository {
}
