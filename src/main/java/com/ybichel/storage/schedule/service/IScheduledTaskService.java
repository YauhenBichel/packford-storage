package com.ybichel.storage.schedule.service;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.ybichel.storage.schedule.entity.ScheduledTask;

import java.util.List;
import java.util.Optional;

public interface IScheduledTaskService {
    Optional<ScheduledTask> findByTaskName(String taskName);
    void save(ScheduledTask task);
    List<ScheduledTask> findAllTaskDueToday();
    void sendPushNotifications(ScheduledTask scheduledTask) throws FirebaseMessagingException;
}
