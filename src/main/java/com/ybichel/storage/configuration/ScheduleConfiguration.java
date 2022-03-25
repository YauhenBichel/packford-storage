package com.ybichel.storage.configuration;

import com.ybichel.storage.jobs.ScheduledTasksRunner;
import com.ybichel.storage.schedule.service.IScheduledTaskService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScheduleConfiguration {

    private IScheduledTaskService scheduledTaskService;

    public ScheduleConfiguration(IScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    @Bean
    public void scheduleTask() {
        ScheduledTasksRunner task = new ScheduledTasksRunner(scheduledTaskService);

        //Timer timer = new Timer();

        int delay = 5 * 60000; // 5 mins
        int period = 1000 * 60 * 60 * 24; //each day

        //timer.schedule(task, delay, period);

        //timer.cancel();
    }
}
