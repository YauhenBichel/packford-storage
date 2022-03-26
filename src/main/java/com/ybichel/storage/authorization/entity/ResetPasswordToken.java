package com.ybichel.storage.authorization.entity;

import com.ybichel.storage.common.model.Audit;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table( name = "reset_password_token")
public class ResetPasswordToken extends Audit {
    private static final int EXPIRATION = 60 * 24; //24 hours in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "token")
    private String token;

    @OneToOne(targetEntity = EmailAccount.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private EmailAccount emailAccount;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public ResetPasswordToken() {
    }

    public ResetPasswordToken(UUID id, String token, EmailAccount emailAccount) {
        this.setId(id);
        this.setToken(token);
        this.setEmailAccount(emailAccount);
        this.setExpiryDate(calculateExpiryDate(EXPIRATION));
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EmailAccount getEmailAccount() {
        return emailAccount;
    }

    public void setEmailAccount(EmailAccount emailAccount) {
        this.emailAccount = emailAccount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
