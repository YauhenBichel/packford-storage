package com.ybichel.storage.schedule.service;

import com.ybichel.storage.schedule.entity.ScheduledTask;

import java.util.List;
import java.util.Optional;

public interface IScheduledTaskService {
    Optional<ScheduledTask> findByTaskName(String taskName);

    void save(ScheduledTask task);

    List<ScheduledTask> findAllTaskDueToday();
}
