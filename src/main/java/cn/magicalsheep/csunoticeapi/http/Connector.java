package cn.magicalsheep.csunoticeapi.http;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.exception.LoginException;
import cn.magicalsheep.csunoticeapi.exception.PageEmptyException;
import cn.magicalsheep.csunoticeapi.model.packet.LoginPacket;
import cn.magicalsheep.csunoticeapi.model.Notice;
import cn.magicalsheep.csunoticeapi.model.packet.Packet;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

public class Connector {

    public URI getURI(String uri) {
        return getURI(uri, null);
    }

    public URI getURI(String uri, Packet packet) {
        URI ret = null;
        if (packet == null) {
            ret = URI.create(uri);
        } else if (packet.getType() == Packet.TYPE.LOGIN) {
            ret = URI.create(String.format(uri + "?%s=%s&%s=%s", "user", ((LoginPacket) packet).getUser(), "pwd", ((LoginPacket) packet).getPwd()));
        }
        return ret;
    }

    private HttpResponse<String> get(URI uri) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .setHeader("User-Agent", "CSU Notice API Bot")
                .build();
        return Factory.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

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

    public ArrayList<Notice> getNotices(int pageNum) throws Exception {
        if (pageNum <= 0) throw new Exception("Invalid page num");
        String uri = Factory.getConfiguration().getRoot_uri();
        if (pageNum > 1)
            uri += "/Home/Release_TZTG/" + (pageNum - 1);
        String html = get(getURI(uri)).body();
        ArrayList<Notice> notices = DOMHandler.translate(html);
        if (notices.isEmpty()) {
            if (!login(Factory.getConfiguration().getUser(), Factory.getConfiguration().getPwd()))
                throw new LoginException("Login Exception: Internal server error");
            notices = DOMHandler.translate(get(getURI(uri)).body());
            if (notices.isEmpty()) throw new PageEmptyException("Page " + pageNum + " is empty");
        }
        return notices;
    }
}
