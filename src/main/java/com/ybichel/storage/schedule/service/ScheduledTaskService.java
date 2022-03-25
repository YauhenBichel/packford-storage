package com.ybichel.storage.schedule.service;

import com.ybichel.storage.account.service.IAccountService;
import com.ybichel.storage.schedule.entity.ScheduledTask;
import com.ybichel.storage.schedule.repository.ScheduledTaskRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduledTaskService implements IScheduledTaskService {

    private static final Logger logger = LogManager.getLogger(ScheduledTaskService.class);

    private final ScheduledTaskRepository scheduledTaskRepository;
    private final IAccountService accountService;

    public ScheduledTaskService(ScheduledTaskRepository scheduledTaskRepository,
                                IAccountService accountService) {
        this.scheduledTaskRepository = scheduledTaskRepository;
        this.accountService = accountService;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<ScheduledTask> findByTaskName(String taskName) {
        return scheduledTaskRepository.findByTaskName(taskName);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<ScheduledTask> findAllTaskDueToday() {
        LocalDateTime localDateTime = LocalDateTime.now().toLocalDate().plusDays(1).atStartOfDay();
        return scheduledTaskRepository.findAllByNextDateTimeBefore(localDateTime);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(ScheduledTask task) {
        scheduledTaskRepository.save(task);
    }

    /*
    ERROR: Transaction silently rolled back because it has been marked as rollback-only
    Possible Fix: replace Propagation.REQUIRED by Propagation.REQUIRES_NEW

    It's not a problem, it's a SPRING feature. "Transaction rolled back because it has been marked as rollback-only" is acceptable.

    Conclusion

    USE REQUIRES_NEW if you want to commit what did you do before exception (Local commit)
    USE REQUIRED if you want to commit only when all processes are done (Global commit) And you just need to ignore "Transaction rolled back because it has been marked as rollback-only" exception. But you need to try-catch out side the caller processNextRegistrationMessage() to have a meaning log.
    Let's me explain more detail:

    Question: How many Transaction we have? Answer: Only one

    Because you config the PROPAGATION is PROPAGATION_REQUIRED so that the @Transaction persist() is using the same transaction with the caller-processNextRegistrationMessage(). Actually, when we get an exception, the Spring will set rollBackOnly for the TransactionManager so the Spring will rollback just only one Transaction.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void sendPushNotifications(ScheduledTask scheduledTask) {
        try {
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    private void sendNotification(UUID accountId) {
    }
}
