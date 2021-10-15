package cn.magicalsheep.csunoticeapi.common.repository;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository {

    Optional<Notice> findNoticeById(int id);

    List<Notice> findAllByIdIsGreaterThan(int head);

    List<Notice> findAllByIdBetween(int st, int ed);

    Optional<Notice> findFirstByOrderByIdDesc();

    Boolean existsByUri(String uri);

    List<Notice> findAllByTitleContains(String title);

}
