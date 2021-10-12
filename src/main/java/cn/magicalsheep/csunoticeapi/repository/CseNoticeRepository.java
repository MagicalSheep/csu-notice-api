package cn.magicalsheep.csunoticeapi.repository;

import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CseNoticeRepository extends JpaRepository<CSENotice, Integer> {

    Optional<Notice> findNoticeById(int id);

    List<Notice> findAllByIdIsGreaterThan(int head);

    List<Notice> findAllByIdBetween(int st, int ed);

    Optional<Notice> findFirstByOrderByIdDesc();

    Boolean existsByUri(String uri);

    Boolean existsByUriAndContentIsNull(String uri);

    List<Notice> findAllByTitleContains(String title);
}
