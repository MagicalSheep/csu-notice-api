package cn.magicalsheep.csunoticeapi.http.connector;

import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.util.ArrayList;

public class CseConnector extends Connector{
    @Override
    public ArrayList<Notice> getNotices(int pageNum) throws Exception {
        // TODO: fetch cse notices
        return null;
    }
}
