package cn.magicalsheep.csunoticeapi.http;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.entity.CSENotice;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.entity.SchoolNotice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DOMHandler {

    private static ArrayList<Notice> handle_school(String html) throws Exception {
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

    private static ArrayList<Notice> handle_cse(String html) {
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[href^=../info], a[href^=../../info]");
        for (Element x : elements) {
            Notice notice = new CSENotice(); // IMPORTANT
            notice.setTitle(x.html());
            notice.setFrom("计算机学院");
            notice.setUri(x.attr("href").replace("..", "https://cse.csu.edu.cn"));
            ret.add(notice);
        }
        return ret;
    }

    public static int getPageNum(Page page) throws NullPointerException {
        if (page.getType() == Page.TYPE.CSE) {
            Document document = Jsoup.parse(page.getContent());
            Element element = document.select("TD[id=\"fanye235272\"]").get(0);
            String str = element.html();
            String res = str.substring(str.substring(0, str.indexOf("/")).length() + 1);
            res = res.replace("&nbsp;", "");
            return Integer.parseInt(res);
        }
        throw new NullPointerException("Invalid page type: Internal server error");
    }

    public static ArrayList<Notice> translate(Page page) throws Exception {
        if (page.getType() == Page.TYPE.SCHOOL) {
            return handle_school(page.getContent());
        } else if (page.getType() == Page.TYPE.CSE) {
            return handle_cse(page.getContent());
        }
        throw new NullPointerException("Invalid page type: Internal server error");
    }

}
