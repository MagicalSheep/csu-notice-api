package cn.magicalsheep.csunoticeapi.school;

import cn.magicalsheep.csunoticeapi.common.util.Factory;
import cn.magicalsheep.csunoticeapi.common.service.impl.BaseHttpService;
import cn.magicalsheep.csunoticeapi.common.exception.LoginException;
import cn.magicalsheep.csunoticeapi.common.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.common.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class SchoolHttpService extends BaseHttpService {

    private static final Logger logger = LoggerFactory.getLogger(SchoolHttpService.class);

    public SchoolHttpService(SchoolStoreService schoolStoreService) {
        super(schoolStoreService, logger, NoticeType.SCHOOL);
    }

    private boolean loginByBrowser(String user, String pwd) {
        LoginPacket loginPacket = new LoginPacket(user, pwd);
        String html = HttpUtils.getByBrowser(HttpUtils.getURI(Factory.getConfiguration().getRootUri() + "/Home/PostLogin", loginPacket));
        return html.contains("1");
    }

    private boolean isLogin(String user, String pwd) {
        LoginPacket loginPacket = new LoginPacket(user, pwd);
        try {
            if (!HttpUtils.get(HttpUtils.getURI(Factory.getConfiguration().getRootUri() + "/Home/PostLogin", loginPacket)).body().equals("1"))
                return true;
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    @Override
    protected int getPageNum() throws Exception {
        if (isLogin(Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
            return 0;
        Document document = Jsoup.parse(HttpUtils.get(
                HttpUtils.getURI(Factory.getConfiguration().getRootUri())).body());
        Element element = document.select("div[id=\"paging1\"]").get(0);
        List<Node> nodes = element.childNodes();
        if (nodes.isEmpty())
            throw new Exception("Internal server error");
        String res = nodes.get(0).toString();
        res = res.substring(res.indexOf(";") + 2, res.indexOf("页"));
        return Integer.parseInt(res);
    }

    @Override
    protected ArrayList<Notice> parse(String html) throws Exception {
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
            notice.setUri(Factory.getConfiguration().getRootUri() + matcher.group(1));
            ret.add(notice);
        }
        return ret;
    }

    @Override
    protected ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getRootUri();
        if (pageNum > 1)
            uri += "/Home/Release_TZTG/" + (pageNum - 1);
        String html = HttpUtils.get(HttpUtils.getURI(uri)).body();
        ArrayList<Notice> notices = parse(html);
        if (notices.isEmpty()) {
            if (isLogin(Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
                throw new LoginException("Login Exception: Internal server error");
            html = HttpUtils.get(HttpUtils.getURI(uri)).body();
            notices = parse(html);
            if (notices.isEmpty()) throw new PageEmptyException("Page " + pageNum + " is empty");
        }
        return notices;
    }

    @Override
    protected NoticeContent fetchContent(Notice notice) {
        String checkHtml = HttpUtils.getByBrowser(HttpUtils.getURI(notice.getUri()));
        if (!checkHtml.contains(notice.getTitle())) {
            if (!loginByBrowser(
                    Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
                return null;
        }
        NoticeContent content = new SchoolNoticeContent();
        content.setUri(notice.getUri());
        content.setContent(HttpUtils.snapshot(notice.getUri()));
        return content;
    }

    @Override
    protected boolean isNeedToUpdate(){
        return Factory.getConfiguration().isSchool();
    }
}
