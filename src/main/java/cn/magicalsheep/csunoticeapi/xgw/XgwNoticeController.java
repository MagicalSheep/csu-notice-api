package cn.magicalsheep.csunoticeapi.xgw;

import cn.magicalsheep.csunoticeapi.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xgw")
public class XgwNoticeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(XgwStoreService.class);

    public XgwNoticeController(XgwStoreService storeService, XgwHttpService httpService) {
        super(logger, storeService, httpService);
    }
}
