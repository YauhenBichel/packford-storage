package com.ybichel.storage.security.entity;

import com.ybichel.storage.common.model.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Data
@Entity
@Table(name = "permission")
@EqualsAndHashCode(callSuper = false)
public class StoragePermission extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "id" )
    private UUID id;

    @Column( name = "name" )
    private String name;
}
