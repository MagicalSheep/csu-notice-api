package cn.magicalsheep.csunoticeapi.common.controller;

import cn.magicalsheep.csunoticeapi.common.model.pojo.NoticeContent;
import cn.magicalsheep.csunoticeapi.common.model.pojo.Notice;
import cn.magicalsheep.csunoticeapi.common.model.AjaxResult;
import cn.magicalsheep.csunoticeapi.common.service.HttpService;
import cn.magicalsheep.csunoticeapi.common.service.StoreService;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;

public class BaseController {

    protected final Logger logger;
    protected final StoreService storeService;
    protected final HttpService httpService;

    public BaseController(
            Logger logger, StoreService storeService, HttpService httpService) {
        this.logger = logger;
        this.storeService = storeService;
        this.httpService = httpService;
    }

    @GetMapping("/page")
    public AjaxResult getNotices(@RequestParam int num) {
        try {
            ArrayList<Notice> res = storeService.getNotices(num);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/head")
    public AjaxResult getHead() {
        return AjaxResult.success(httpService.getHEAD());
    }

    @GetMapping
    public AjaxResult getDeltaNotices(@RequestParam int head) {
        try {
            ArrayList<Notice> res = storeService.getDeltaNotices(head);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/notice")
    public AjaxResult getNotice(@RequestParam int id) {
        try {
            return AjaxResult.success(storeService.getNoticeById(id));
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public AjaxResult getLatestNotice() {
        try {
            return AjaxResult.success(storeService.getLatestNotice());
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public AjaxResult getNoticeByTitle(@RequestParam String title) {
        return AjaxResult.success(storeService.getNoticeByTitle(title));
    }

    @GetMapping("/content")
    public AjaxResult getNoticeContentByNoticeId(@RequestParam int id) {
        NoticeContent res = storeService.getNoticeContentByNoticeId(id);
        if (res != null)
            res.setId(id);
        return AjaxResult.success(res);
    }
}
