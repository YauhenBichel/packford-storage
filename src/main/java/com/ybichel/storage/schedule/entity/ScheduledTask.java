package com.ybichel.storage.schedule.entity;

import com.ybichel.storage.common.model.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name="scheduled_task")
@EqualsAndHashCode(callSuper = false)
public class ScheduledTask extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column( name = "task_name" )
    @NotBlank
    private String taskName;

    @Column( name = "next_date_time" )
    @NotNull
    private LocalDateTime nextDateTime;
}
