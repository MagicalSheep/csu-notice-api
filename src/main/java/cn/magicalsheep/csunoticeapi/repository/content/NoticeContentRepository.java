package cn.magicalsheep.csunoticeapi.repository.content;

import cn.magicalsheep.csunoticeapi.model.pojo.content.NoticeContent;

import java.util.Optional;

public interface NoticeContentRepository {

    Optional<NoticeContent> findNoticeContentByUri(String uri);

    Boolean existsByUri(String uri);
}
