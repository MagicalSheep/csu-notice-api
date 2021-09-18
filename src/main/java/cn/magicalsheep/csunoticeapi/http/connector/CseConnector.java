package cn.magicalsheep.csunoticeapi.http.connector;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.http.DOMHandler;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.util.ArrayList;

public class CseConnector extends Connector {

    @Override
    public ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getCse_uri();
        Page page = new Page(Page.TYPE.CSE, get(getURI(uri)).body());
        int totPage = DOMHandler.getPageNum(page);
        if (pageNum > 1) {
            uri = uri.replace(".htm", "/" + (totPage - pageNum + 1) + ".htm");
            page = new Page(Page.TYPE.CSE, get(getURI(uri)).body());
        }
        ArrayList<Notice> notices = DOMHandler.translate(page);
        if (notices.isEmpty())
            throw new PageEmptyException("Page " + pageNum + " is empty");
        return notices;
    }
}
