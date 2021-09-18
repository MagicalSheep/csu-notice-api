package cn.magicalsheep.csunoticeapi.api;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.model.Page;
import cn.magicalsheep.csunoticeapi.model.response.NoticeResponse;
import cn.magicalsheep.csunoticeapi.model.response.NoticesResponse;
import cn.magicalsheep.csunoticeapi.model.response.Response;
import cn.magicalsheep.csunoticeapi.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class APIHandler {

    private final Logger logger = LoggerFactory.getLogger(APIHandler.class);
    private final StoreService storeService;

    public APIHandler(StoreService storeService) {
        this.storeService = storeService;
        if (Factory.isFirstRun()) {
            logger.error("Please finish the settings.json to run API");
            return;
        }
        new Thread(() -> {
            try {
                if (Factory.getConfiguration().isInit_db())
                    storeService.updateAll();
                else
                    storeService.update(Factory.getConfiguration().getUpdate_num_per_pages());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }).start();
    }

    @PostMapping("/main/page")
    public NoticesResponse getNotices(@RequestParam int num) {
        try {
            return new NoticesResponse(storeService.getNotices(Page.TYPE.SCHOOL, num));
        } catch (Exception e) {
            NoticesResponse ret = new NoticesResponse(null);
            ret.setStatus(0);
            ret.setMsg(e.getMessage());
            return ret;
        }
    }

    @GetMapping("/main/head")
    public Response getHead() {
        return new Response(storeService.getHEAD(Page.TYPE.SCHOOL));
    }

    @PostMapping("/main")
    public NoticesResponse getDeltaNotices(@RequestParam int head) {
        try {
            return new NoticesResponse(storeService.getDeltaNotices(Page.TYPE.SCHOOL, head));
        } catch (Exception e) {
            NoticesResponse noticesResponse = new NoticesResponse(null);
            noticesResponse.setStatus(0);
            noticesResponse.setMsg(e.getMessage());
            return noticesResponse;
        }
    }

    @PostMapping("/main/notice")
    public NoticeResponse getNotice(@RequestParam int id) {
        try {
            return new NoticeResponse(storeService.getNoticeById(Page.TYPE.SCHOOL, id));
        } catch (Exception e) {
            NoticeResponse noticeResponse = new NoticeResponse(null);
            noticeResponse.setStatus(0);
            noticeResponse.setMsg(e.getMessage());
            return noticeResponse;
        }
    }

    @GetMapping("/main/latest")
    public NoticeResponse getLatestNotice() {
        try {
            return new NoticeResponse(storeService.getLatestNotice(Page.TYPE.SCHOOL));
        } catch (Exception e) {
            NoticeResponse noticeResponse = new NoticeResponse(null);
            noticeResponse.setStatus(0);
            noticeResponse.setMsg(e.getMessage());
            return noticeResponse;
        }
    }

}
