package com.ybichel.storage.authorization.entity;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.common.model.Audit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
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

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "account_id")
    @Fetch(FetchMode.JOIN)
    private Account account;
}
