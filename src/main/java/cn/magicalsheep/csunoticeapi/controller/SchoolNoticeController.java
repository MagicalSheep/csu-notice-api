package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.service.impl.http.SchoolHttpService;
import cn.magicalsheep.csunoticeapi.service.impl.store.SchoolStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class SchoolNoticeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(SchoolNoticeController.class);

    public SchoolNoticeController(SchoolStoreService storeService, SchoolHttpService httpService) {
        super(logger, storeService, httpService);
    }
}
