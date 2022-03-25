package com.ybichel.storage.account.entity;

import com.ybichel.storage.common.model.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Entity
@Table(name = "email_account")
@EqualsAndHashCode(callSuper = false)
public class EmailAccount extends Audit {
    @Id
    @Column(name = "id")
    private UUID id;

    @NotBlank
    @Size(min = 3)
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Column(name = "verificated")
    private Boolean verificated = Boolean.FALSE;
}
