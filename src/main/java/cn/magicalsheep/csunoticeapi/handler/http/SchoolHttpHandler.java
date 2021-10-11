package cn.magicalsheep.csunoticeapi.handler.http;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.LoginException;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.handler.HttpHandler;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SchoolHttpHandler extends HttpHandler {

    private static final NoticeType type = NoticeType.SCHOOL;

    private boolean login(String user, String pwd) {
        LoginPacket loginPacket = new LoginPacket(user, pwd);
        try {
            if (!get(HttpUtils.getURI(Factory.getConfiguration().getRoot_uri() + "/Home/PostLogin", loginPacket)).body().equals("1"))
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected ArrayList<Notice> parse(Page page) throws Exception{
        String html = page.getContent();
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[class=\"lineheight\"]");
        for (Element element : elements) {
            Notice notice = new SchoolNotice(); // IMPORTANT
            Matcher matcher = Pattern.compile("\\[(.*?)]").matcher(element.attr("title"));
            notice.setFrom((matcher.find()) ? matcher.group(1) : "");
            notice.setTitle(element.attr("title").replaceFirst("(\\[(.*?)])", ""));
            matcher = Pattern.compile("'(.*?)'").matcher(element.attr("onclick"));
            if (!matcher.find()) throw new Exception("URI is empty!");
            notice.setUri(Factory.getConfiguration().getRoot_uri() + matcher.group(1));
            ret.add(notice);
        }
        return ret;
    }

    @Override
    public ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getRoot_uri();
        if (pageNum > 1)
            uri += "/Home/Release_TZTG/" + (pageNum - 1);
        Page page = new Page(type, get(HttpUtils.getURI(uri)).body());
        ArrayList<Notice> notices = parse(page);
        if (notices.isEmpty()) {
            if (!login(Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
                throw new LoginException("Login Exception: Internal server error");
            page.setContent(get(HttpUtils.getURI(uri)).body());
            notices = parse(page);
            if (notices.isEmpty()) throw new PageEmptyException("Page " + pageNum + " is empty");
        }
        return notices;
    }
}
