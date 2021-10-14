package cn.magicalsheep.csunoticeapi.repository.notice;

import cn.magicalsheep.csunoticeapi.model.pojo.notice.SchoolNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolNoticeRepository
        extends JpaRepository<SchoolNotice, Integer>, NoticeRepository {
}
