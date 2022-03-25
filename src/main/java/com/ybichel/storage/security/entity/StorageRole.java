package com.ybichel.storage.security.entity;

import com.ybichel.storage.common.model.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = false)
public class StorageRole extends Audit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "id" )
    private UUID id;

    @Column( name = "name" )
    private String name;

    @ManyToMany( fetch = FetchType.LAZY )
    @JoinTable(
            name = "role_permission",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "permission_id", referencedColumnName = "id"))
    private Set<StoragePermission> permissions = new HashSet<>();
}
