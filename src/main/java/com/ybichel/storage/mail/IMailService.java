package com.ybichel.storage.mail;

import com.ybichel.storage.authorization.entity.EmailAccount;

public interface IMailService {
    void confirmRegistration(EmailAccount emailAccount);

    void resetPassword(EmailAccount emailAccount);
}
