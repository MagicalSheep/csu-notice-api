package cn.magicalsheep.csunoticeapi.common.task;

import cn.magicalsheep.csunoticeapi.common.model.Configuration;
import cn.magicalsheep.csunoticeapi.cse.CSEHttpService;
import cn.magicalsheep.csunoticeapi.mail.MailHttpService;
import cn.magicalsheep.csunoticeapi.school.SchoolHttpService;
import cn.magicalsheep.csunoticeapi.xgw.XgwHttpService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateTask {

    private final Logger logger = LoggerFactory.getLogger(UpdateTask.class);
    private final SchoolHttpService schoolHttpService;
    private final CSEHttpService cseHttpService;
    private final MailHttpService mailHttpService;
    private final XgwHttpService xgwHttpService;

    @Scheduled(cron = "0 */10 * * * ?")
    public void update() {
        if (Configuration.getBooleanProperties("initialization"))
            return;
        int pageNum = Configuration.getIntegerProperties("update_pages_num");
        if (Configuration.getBooleanProperties("update_school_notice")) {
            new Thread(() -> {
                try {
                    schoolHttpService.update(pageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Configuration.getBooleanProperties("update_cse_notice")) {
            new Thread(() -> {
                try {
                    cseHttpService.update(pageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Configuration.getBooleanProperties("update_headmaster_mail")) {
            new Thread(() -> {
                try {
                    mailHttpService.update(pageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Configuration.getBooleanProperties("update_xgw_notice")) {
            new Thread(() -> {
                try {
                    xgwHttpService.update(pageNum);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
    }
}
