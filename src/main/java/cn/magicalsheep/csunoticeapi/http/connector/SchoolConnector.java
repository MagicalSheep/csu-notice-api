package cn.magicalsheep.csunoticeapi.http.connector;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.LoginException;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.http.DOMHandler;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;

import java.util.ArrayList;

public class SchoolConnector extends Connector {

    private boolean login(String user, String pwd) {
        LoginPacket loginPacket = new LoginPacket(user, pwd);
        try {
            if (!get(getURI(Factory.getConfiguration().getRoot_uri() + "/Home/PostLogin", loginPacket)).body().equals("1"))
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getRoot_uri();
        if (pageNum > 1)
            uri += "/Home/Release_TZTG/" + (pageNum - 1);
        Page page = new Page(Page.TYPE.SCHOOL, get(getURI(uri)).body());
        ArrayList<Notice> notices = DOMHandler.translate(page);
        if (notices.isEmpty()) {
            if (!login(Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
                throw new LoginException("Login Exception: Internal server error");
            page.setContent(get(getURI(uri)).body());
            notices = DOMHandler.translate(page);
            if (notices.isEmpty()) throw new PageEmptyException("Page " + pageNum + " is empty");
        }
        return notices;
    }
}
