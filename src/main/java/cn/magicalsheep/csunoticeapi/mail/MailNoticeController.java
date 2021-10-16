package cn.magicalsheep.csunoticeapi.mail;

import cn.magicalsheep.csunoticeapi.common.controller.BaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
public class MailNoticeController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MailNoticeController.class);

    public MailNoticeController(MailStoreService storeService, MailHttpService httpService) {
        super(logger, storeService, httpService);
    }
}
