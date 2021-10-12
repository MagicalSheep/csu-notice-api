package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.response.AjaxResult;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import cn.magicalsheep.csunoticeapi.service.impl.http.CSEHttpService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/cse")
@AllArgsConstructor
public class CSENoticeController {

    private static final Logger logger = LoggerFactory.getLogger(CSENoticeController.class);
    private final StoreService storeService;
    private final CSEHttpService cseHttpService;
    private static final NoticeType type = NoticeType.CSE;

    @PostMapping("/page")
    public AjaxResult getCSENotices(@RequestParam int num) {
        try {
            ArrayList<Notice> res = storeService.getNotices(type, num);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/head")
    public AjaxResult getCSEHead() {
        return AjaxResult.success(cseHttpService.getHEAD());
    }

    @PostMapping
    public AjaxResult getCSEDeltaNotices(@RequestParam int head) {
        try {
            ArrayList<Notice> res = storeService.getDeltaNotices(type, head);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/notice")
    public AjaxResult getCSENotice(@RequestParam int id) {
        try {
            Notice res = storeService.getNoticeById(type, id);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public AjaxResult getCSELatestNotice() {
        try {
            Notice res = storeService.getLatestNotice(type);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/query")
    public AjaxResult getNoticeByTitle(@RequestParam String title) {
        return AjaxResult.success(storeService.getNoticeByTitle(type, title));
    }
}
