package cn.magicalsheep.csunoticeapi.service;

import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.pojo.notice.Notice;

import java.util.ArrayList;

public interface HttpService {

    ArrayList<Notice> parse(Page page) throws Exception;

    int getHEAD();

    void update(int updatePageNum) throws Exception;

}
