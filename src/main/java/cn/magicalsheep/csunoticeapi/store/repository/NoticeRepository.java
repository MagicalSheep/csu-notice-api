package cn.magicalsheep.csunoticeapi.store.repository;

import cn.magicalsheep.csunoticeapi.model.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    Optional<Notice> findNoticeById(int id);

    List<Notice> findAllByIdIsGreaterThan(int head);

    List<Notice> findAllByIdBetween(int st, int ed);

    Optional<Notice> findFirstByOrderByIdDesc();

    Optional<Notice> findNoticeByUri(String uri);

}
