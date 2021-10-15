package cn.magicalsheep.csunoticeapi.common.repository;

import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;

import java.util.Optional;

public interface NoticeContentRepository {

    Optional<NoticeContent> findNoticeContentByUri(String uri);

    Boolean existsByUriAndContentNotNull(String uri);
}
