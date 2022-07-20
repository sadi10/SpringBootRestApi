package com.synesisit.gpvas.config;

import com.synesisit.gpvas.helper.SchedulerHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ScheduleConfig {

    @Autowired
    SchedulerHelper schedulerHelper;

    @Scheduled(fixedDelay = 2000)
    public void scheduleFixedDelayTask() {
        System.out.println("Process Incoming SMS Task - " + System.currentTimeMillis() / 1000);
        schedulerHelper.processIncomingSMS();
    }
}
