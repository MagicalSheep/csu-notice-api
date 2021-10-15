package cn.magicalsheep.csunoticeapi.school;

import cn.magicalsheep.csunoticeapi.common.repository.NoticeRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolNoticeRepository
        extends JpaRepository<SchoolNotice, Integer>, NoticeRepository {
}
