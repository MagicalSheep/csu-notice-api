package cn.magicalsheep.csunoticeapi.handler;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public abstract class HttpHandler {

    protected HttpResponse<String> get(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .setHeader("User-Agent", "CSU Notice API Bot")
                .build();
        return Factory.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected String toBase64Image(String html) {
        return null;
    }

    protected abstract ArrayList<Notice> parse(Page page) throws Exception;

    public abstract ArrayList<Notice> getNotices(int pageNum) throws Exception;
}
