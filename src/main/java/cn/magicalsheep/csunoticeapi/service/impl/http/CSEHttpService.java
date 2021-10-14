package cn.magicalsheep.csunoticeapi.service.impl.http;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import cn.magicalsheep.csunoticeapi.service.impl.store.CSEStoreService;
import cn.magicalsheep.csunoticeapi.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CSEHttpService extends BaseHttpService {

    public CSEHttpService(CSEStoreService cseStoreService) {
        super(cseStoreService, NoticeType.CSE);
    }

    private int getPageNum(Page page) {
        Document document = Jsoup.parse(page.getContent());
        Element element = document.select("TD[id=\"fanye235272\"]").get(0);
        String str = element.html();
        String res = str.substring(str.substring(0, str.indexOf("/")).length() + 1);
        res = res.replace("&nbsp;", "");
        return Integer.parseInt(res);
    }

    @Override
    public ArrayList<Notice> parse(Page page) {
        String html = page.getContent();
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[href^=../info], a[href^=../../info]");
        for (Element x : elements) {
            Notice notice = new CSENotice(); // IMPORTANT
            notice.setTitle(x.html());
            notice.setFrom("计算机学院");
            String url = x.attr("href");
            if (url.startsWith("../info"))
                notice.setUri(url.replace("..", "https://cse.csu.edu.cn"));
            else
                notice.setUri(url.replace("../..", "https://cse.csu.edu.cn"));
            ret.add(notice);
        }
        return ret;
    }

    @Override
    protected ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getCse_uri();
        Page page = new Page(type, HttpUtils.get(HttpUtils.getURI(uri)).body());
        int totPage = getPageNum(page);
        if (pageNum > 1) {
            uri = uri.replace(".htm", "/" + (totPage - pageNum + 1) + ".htm");
            page = new Page(type, HttpUtils.get(HttpUtils.getURI(uri)).body());
        }
        ArrayList<Notice> notices = parse(page);
        if (notices.isEmpty())
            throw new PageEmptyException("Page " + pageNum + " is empty");
        return notices;
    }

    @Override
    protected String fetchContent(Notice notice) {
        return HttpUtils.snapshot(notice.getUri());
    }
}
