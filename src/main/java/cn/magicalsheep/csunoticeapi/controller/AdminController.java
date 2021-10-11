package cn.magicalsheep.csunoticeapi.controller;

import cn.magicalsheep.csunoticeapi.Factory;
import cn.magicalsheep.csunoticeapi.service.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    public AdminController(StoreService storeService) {
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
}
