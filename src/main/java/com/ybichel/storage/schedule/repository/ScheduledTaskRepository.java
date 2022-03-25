package com.ybichel.storage.schedule.repository;

import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import com.ybichel.storage.schedule.entity.ScheduledTask;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface ScheduledTaskRepository extends SpecificationPagingAndSortingRepository<ScheduledTask, UUID> {
    Optional<ScheduledTask> findByTaskName(String taskName);
    List<ScheduledTask> findAllByNextDateTimeBefore(LocalDateTime localDateTime);
}
