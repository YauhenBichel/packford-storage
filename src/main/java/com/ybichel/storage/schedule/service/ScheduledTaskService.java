package com.ybichel.storage.schedule.service;

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

@Service
public class ScheduledTaskService implements IScheduledTaskService {
    private final ScheduledTaskRepository scheduledTaskRepository;

    public ScheduledTaskService(ScheduledTaskRepository scheduledTaskRepository) {
        this.scheduledTaskRepository = scheduledTaskRepository;
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
}
