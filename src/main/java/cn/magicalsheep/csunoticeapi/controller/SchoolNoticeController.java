package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.response.AjaxResult;
import cn.magicalsheep.csunoticeapi.service.impl.http.SchoolHttpService;
import cn.magicalsheep.csunoticeapi.service.impl.store.SchoolStoreService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/main")
@AllArgsConstructor
public class SchoolNoticeController {

    private static final Logger logger = LoggerFactory.getLogger(SchoolNoticeController.class);
    private final SchoolStoreService storeService;
    private final SchoolHttpService schoolHttpService;

    @PostMapping("/page")
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
        return AjaxResult.success(schoolHttpService.getHEAD());
    }

    @PostMapping
    public AjaxResult getDeltaNotices(@RequestParam int head) {
        try {
            ArrayList<Notice> res = storeService.getDeltaNotices(head);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/notice")
    public AjaxResult getNotice(@RequestParam int id) {
        try {
            Notice res = storeService.getNoticeById(id);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public AjaxResult getLatestNotice() {
        try {
            Notice res = storeService.getLatestNotice();
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/query")
    public AjaxResult getNoticeByTitle(@RequestParam String title) {
        return AjaxResult.success(storeService.getNoticeByTitle(title));
    }

}
