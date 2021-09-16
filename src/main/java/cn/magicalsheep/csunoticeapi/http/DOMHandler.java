package cn.magicalsheep.csunoticeapi.http;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Notice;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DOMHandler {

    public static ArrayList<Notice> translate(String html) throws Exception {
        ArrayList<Notice> ret = new ArrayList<>();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("a[class=\"lineheight\"]");
        for (Element element : elements) {
            Notice notice = new Notice();
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

}
