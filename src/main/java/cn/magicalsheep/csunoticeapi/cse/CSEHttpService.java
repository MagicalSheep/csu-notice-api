package cn.magicalsheep.csunoticeapi.cse;

import cn.magicalsheep.csunoticeapi.common.util.Factory;
import cn.magicalsheep.csunoticeapi.common.service.impl.BaseHttpService;
import cn.magicalsheep.csunoticeapi.common.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.common.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.util.HttpUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CSEHttpService extends BaseHttpService {

    private static final Logger logger = LoggerFactory.getLogger(CSEHttpService.class);

    public CSEHttpService(CSEStoreService cseStoreService) {
        super(cseStoreService, logger, NoticeType.CSE);
    }

    @Override
    protected int getPageNum() throws Exception {
        String uri = Factory.getConfiguration().getCseUri();
        Document document = Jsoup.parse(HttpUtils.get(HttpUtils.getURI(uri)).body());
        Element element = document.select("TD[id=\"fanye235272\"]").get(0);
        String str = element.html();
        String res = str.substring(str.substring(0, str.indexOf("/")).length() + 1);
        res = res.replace("&nbsp;", "");
        return Integer.parseInt(res);
    }

    @Override
    protected boolean isNeedToUpdate() {
        return Factory.getConfiguration().isCse();
    }

    @Override
    protected ArrayList<Notice> parse(String html) {
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
        String uri = Factory.getConfiguration().getCseUri();
        String html = HttpUtils.get(HttpUtils.getURI(uri)).body();
        int totPage = getPageNum();
        if (pageNum > 1) {
            uri = uri.replace(".htm", "/" + (totPage - pageNum + 1) + ".htm");
            html = HttpUtils.get(HttpUtils.getURI(uri)).body();
        }
        ArrayList<Notice> notices = parse(html);
        if (notices.isEmpty())
            throw new PageEmptyException("Page " + pageNum + " is empty");
        return notices;
    }

    @Override
    protected NoticeContent fetchContent(Notice notice) {
        NoticeContent content = new CSENoticeContent();
        content.setUri(notice.getUri());
        content.setContent(HttpUtils.snapshot(notice.getUri()));
        return content;
    }

}
