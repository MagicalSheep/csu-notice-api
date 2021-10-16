package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.common.model.Configuration;
import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.service.impl.BaseHttpService;
import cn.magicalsheep.csunoticeapi.common.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MailHttpService extends BaseHttpService {

    private static final Logger logger = LoggerFactory.getLogger(MailHttpService.class);

    public MailHttpService(MailStoreService mailStoreService) {
        super(mailStoreService, logger, NoticeType.MAIL);
    }

    @Override
    protected int getPageNum() throws Exception {
        Document document = Jsoup.parse(HttpUtils.get(
                HttpUtils.getURI(Configuration.getProperties("headmaster_mail_url"))).body());
        Element element = document.select("div.page").get(0);
        element = (Element) element.childNodes().get(3);
        String res = ((TextNode) element.childNodes().get(0)).text();
        return Integer.parseInt(res);
    }

    @Override
    protected boolean isNeedToUpdate() {
        return Configuration.getBooleanProperties("update_headmaster_mail");
    }

    @Override
    protected ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String url = Configuration.getProperties("headmaster_mail_url") + "?ps=" + pageNum;
        String html = HttpUtils.get(HttpUtils.getURI(url)).body();
        ArrayList<Notice> notices = parse(html);
        if (notices.isEmpty())
            throw new PageEmptyException("Page " + pageNum + " is empty");
        return notices;
    }

    @Override
    protected NoticeContent fetchContent(Notice notice) {
        NoticeContent content = new MailNoticeContent();
        content.setUri(notice.getUri());
        content.setContent(HttpUtils.snapshot(notice.getUri()));
        return content;
    }

    @Override
    protected ArrayList<Notice> parse(String html) throws Exception {
        String baseUrl = Configuration.getProperties("headmaster_mail_url");
        baseUrl = baseUrl.substring(0, baseUrl.indexOf("MailList_Pub"));
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[target=\"_blank\"]");
        for (Element element : elements) {
            String url = element.attr("href");
            if (url.equals("MailIn.aspx") || url.equals("http://www.csu.edu.cn/"))
                continue;
            Notice notice = new MailNotice();
            notice.setTitle(element.attr("title"));
            notice.setFrom("校长信箱");
            notice.setUri(baseUrl + url);
            ret.add(notice);
        }
        return ret;
    }
}
