package com.ybichel.storage.jobs;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ybichel.storage.common.Constants;
import com.ybichel.storage.schedule.entity.ScheduledTask;
import com.ybichel.storage.schedule.service.IScheduledTaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.TimerTask;
import java.util.List;

@Component
public class ScheduledTasksRunner extends TimerTask {

    private static final Logger logger = LogManager.getLogger(ScheduledTasksRunner.class);

    private final IScheduledTaskService scheduledTaskService;

    public ScheduledTasksRunner(IScheduledTaskService scheduledTaskService) {
        this.scheduledTaskService = scheduledTaskService;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void run() {
        List<ScheduledTask> scheduledTasks = scheduledTaskService.findAllTaskDueToday();

        logger.info("Scheduled tasks amount = {}", scheduledTasks.size());

        scheduledTasks.forEach(scheduledTask -> {
            if (scheduledTask.getTaskName().equals(Constants.SCHEDULED_TASK_SEND_PUSH_NOTIFICATIONS)) {
                try {
                    //scheduledTaskService(scheduledTask);
                } catch (Exception ex) {
                    logger.error(ex);
                }
            }
        });
    }
}
