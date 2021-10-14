package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.service.impl.http.CSEHttpService;
import cn.magicalsheep.csunoticeapi.service.impl.store.CSEStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cse")
public class CSENoticeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CSENoticeController.class);

    public CSENoticeController(CSEStoreService storeService, CSEHttpService httpService) {
        super(logger, storeService, httpService);
    }
}
