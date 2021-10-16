package cn.magicalsheep.csunoticeapi.common.task;

import cn.magicalsheep.csunoticeapi.common.util.Factory;
import cn.magicalsheep.csunoticeapi.cse.CSEHttpService;
import cn.magicalsheep.csunoticeapi.school.SchoolHttpService;
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

    @Scheduled(cron = "0 */10 * * * ?")
    public void update() {
        int page_num = Factory.getConfiguration().getUpdateNumPerPages();
        if (Factory.getConfiguration().isSchool()) {
            new Thread(() -> {
                try {
                    schoolHttpService.update(page_num);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
        if (Factory.getConfiguration().isCse()) {
            new Thread(() -> {
                try {
                    cseHttpService.update(page_num);
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }).start();
        }
    }
}
