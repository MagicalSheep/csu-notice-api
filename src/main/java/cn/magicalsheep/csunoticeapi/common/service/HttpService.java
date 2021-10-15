package cn.magicalsheep.csunoticeapi.common.service;

import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;

import java.util.ArrayList;

public interface HttpService {

    ArrayList<Notice> parse(String html) throws Exception;

    int getHEAD();

    void update(int updatePageNum) throws Exception;

    void updateAll() throws Exception;

}
