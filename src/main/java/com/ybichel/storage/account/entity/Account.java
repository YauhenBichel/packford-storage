package com.ybichel.storage.account.entity;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.common.PostgreSQLEnumType;
import com.ybichel.storage.common.model.Audit;
import com.ybichel.storage.security.entity.StorageRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.TypeDef;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table( name = "account")
@TypeDef(
        name = "pgsql_enum",
        typeClass = PostgreSQLEnumType.class
)
@BatchSize( size = 100 )
@EqualsAndHashCode(callSuper = false)
public class Account extends Audit {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<StorageRole> roles = new HashSet<>();
}
