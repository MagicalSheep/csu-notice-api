package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.model.constant.NoticeType;
import cn.magicalsheep.csunoticeapi.model.entity.Notice;
import cn.magicalsheep.csunoticeapi.model.response.AjaxResult;
import cn.magicalsheep.csunoticeapi.service.StoreService;
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
    private final StoreService storeService;
    private static final NoticeType type = NoticeType.SCHOOL;

    @PostMapping("/page")
    public AjaxResult getNotices(@RequestParam int num) {
        try {
            ArrayList<Notice> res = storeService.getNotices(type, num);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/head")
    public AjaxResult getHead() {
        return AjaxResult.success(storeService.getHEAD(type));
    }

    @PostMapping
    public AjaxResult getDeltaNotices(@RequestParam int head) {
        try {
            ArrayList<Notice> res = storeService.getDeltaNotices(type, head);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/notice")
    public AjaxResult getNotice(@RequestParam int id) {
        try {
            Notice res = storeService.getNoticeById(type, id);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public AjaxResult getLatestNotice() {
        try {
            Notice res = storeService.getLatestNotice(type);
            return AjaxResult.success(res);
        } catch (Exception e) {
            logger.info("[Error] " + e.getMessage());
            return AjaxResult.error(e.getMessage());
        }
    }
}
