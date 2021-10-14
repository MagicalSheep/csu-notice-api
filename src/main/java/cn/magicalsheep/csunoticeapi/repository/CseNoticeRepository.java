package cn.magicalsheep.csunoticeapi.repository;

import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CseNoticeRepository extends JpaRepository<CSENotice, Integer>, Repository {
}
