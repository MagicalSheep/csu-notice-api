package cn.magicalsheep.csunoticeapi.common.service;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import java.util.ArrayList;

public interface HttpService {

    int getHEAD();

    void update(int updatePageNum) throws Exception;

    void updateAll() throws Exception;

    void loadContent(Notice notice, boolean isForce);

    void loadContent(Notice notice);

    void loadContent(ArrayList<Notice> notices);

}
