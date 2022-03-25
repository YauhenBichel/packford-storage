package com.ybichel.storage.authorization.entity;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.common.model.Audit;

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

@Entity
@Table( name = "verification_token")
public class VerificationToken extends Audit {
    private static final int EXPIRATION = 60 * 24; //24 hours in minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column( name = "id" )
    private UUID id;

    @Column( name = "token" )
    private String token;

    @OneToOne(targetEntity = Account.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "account_id")
    private Account account;

    @Column(name = "expiry_date")
    private Date expiryDate;

    public VerificationToken() {
    }

    public VerificationToken(UUID id, String token, Account account) {
        this.setId(id);
        this.setToken(token);
        this.setAccount(account);
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
    public Account getAccount() {
        return account;
    }
    public void setAccount(Account account) {
        this.account = account;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerificationToken)) return false;

        VerificationToken that = (VerificationToken) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (token != null ? !token.equals(that.token) : that.token != null) return false;
        if (account != null ? !account.equals(that.account) : that.account != null) return false;
        return expiryDate != null ? expiryDate.equals(that.expiryDate) : that.expiryDate == null;
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (token != null ? token.hashCode() : 0);
        result = 31 * result + (account != null ? account.hashCode() : 0);
        result = 31 * result + (expiryDate != null ? expiryDate.hashCode() : 0);
        return result;
    }

    private Date calculateExpiryDate(int expiryTimeInMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
}
