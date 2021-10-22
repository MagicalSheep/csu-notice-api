package cn.magicalsheep.csunoticeapi.xgw;

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
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class XgwHttpService extends BaseHttpService {

    private static final Logger logger = LoggerFactory.getLogger(XgwStoreService.class);

    public XgwHttpService(XgwStoreService storeService) {
        super(storeService, logger, NoticeType.XGW);
    }

    @Override
    protected int getPageNum() throws Exception {
        String uri = Configuration.getProperties("xgw_notice_url");
        Document document = Jsoup.parse(HttpUtils.get(HttpUtils.getURI(uri)).body());
        Elements elements = document.select("div.pb_sys_common");
        assert elements.size() != 0;
        Element element = (Element) elements.get(0).childNodes().get(0);
        String totStr = element.childNodes().get(0).toString();
        int totNoticeNum = Integer.parseInt(totStr.substring(1, totStr.length() - 1));
        return (int) Math.ceil(totNoticeNum / 20.0);
    }

    @Override
    protected boolean isNeedToUpdate() {
        return Configuration.getBooleanProperties("update_xgw_notice");
    }

    @Override
    protected ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Configuration.getProperties("xgw_notice_url");
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
        NoticeContent content = new XgwNoticeContent();
        content.setUri(notice.getUri());
        content.setContent(HttpUtils.snapshot(notice.getUri()));
        return content;
    }

    @Override
    protected ArrayList<Notice> parse(String html) throws Exception {
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[href^=info], a[href^=../info]");
        for (Element x : elements) {
            Notice notice = new XgwNotice();
            notice.setTitle(x.html());
            notice.setFrom("学工部");
            String url = x.attr("href");
            String baseUrl = Configuration.getProperties("xgw_notice_url");
            baseUrl = baseUrl.replaceAll("/tzgg.htm", "");
            if (url.startsWith("../info"))
                notice.setUri(url.replace("..", baseUrl));
            else
                notice.setUri(baseUrl + "/" + url);
            ret.add(notice);
        }
        return ret;
    }
}
