package cn.magicalsheep.csunoticeapi.api;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Notice;
import cn.magicalsheep.csunoticeapi.model.response.NoticeResponse;
import cn.magicalsheep.csunoticeapi.model.response.NoticesResponse;
import cn.magicalsheep.csunoticeapi.model.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class APIHandler {

    @PostMapping("/main/page")
    public NoticesResponse getNotices(@RequestParam int num) {
        try {
            return new NoticesResponse(Factory.getRepository().getNotices(num));
        } catch (Exception e) {
            NoticesResponse ret = new NoticesResponse(null);
            ret.setStatus(0);
            ret.setMsg(e.getMessage());
            return ret;
        }
    }

    @GetMapping("/main/head")
    public Response getHead() {
        return new Response(Factory.getRepository().getHead());
    }

    @PostMapping("/main")
    public NoticesResponse getDeltaNotices(@RequestParam int head) {
        int latestHead = Factory.getRepository().getHead();
        if (head >= latestHead || head < 0) {
            NoticesResponse noticesResponse = new NoticesResponse(null);
            noticesResponse.setStatus(0);
            noticesResponse.setMsg("Nothing to fetch");
            return noticesResponse;
        }
        ArrayList<Notice> notices = new ArrayList<>();
        for (int i = head + 1; i <= latestHead; i++)
            notices.add(Factory.getRepository().getNoticeById(i));
        return new NoticesResponse(notices);
    }

    @PostMapping("/main/notice")
    public NoticeResponse getNotice(@RequestParam int id) {
        return new NoticeResponse(Factory.getRepository().getNoticeById(id));
    }

    @GetMapping("/main/latest")
    public NoticeResponse getLatestNotice() {
        return new NoticeResponse(Factory.getRepository().getNoticeById(Factory.getRepository().getHead()));
    }

}
