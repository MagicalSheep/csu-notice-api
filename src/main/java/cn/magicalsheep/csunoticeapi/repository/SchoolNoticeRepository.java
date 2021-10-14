package cn.magicalsheep.csunoticeapi.repository;

import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolNoticeRepository extends JpaRepository<SchoolNotice, Integer>, Repository {
}
