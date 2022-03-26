package com.ybichel.storage.mail;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.entity.EmailAccount;

public interface IMailService {
    void confirmRegistration(EmailAccount emailAccount);
    void resetPassword(Account account);
}
