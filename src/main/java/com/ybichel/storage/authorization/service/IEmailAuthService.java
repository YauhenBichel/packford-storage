package com.ybichel.storage.authorization.service;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.model.AuthenticatedAccount;
import com.ybichel.storage.authorization.vo.ConfirmRegistrationResponseVO;
import com.ybichel.storage.authorization.vo.LoginRequestVO;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import com.ybichel.storage.authorization.vo.EmailResetPasswordRequestVO;
import com.ybichel.storage.authorization.vo.ResetPasswordRequestVO;

import java.util.Optional;
import java.util.UUID;

public interface IEmailAuthService {
    Account register(UUID userId, RegistrationRequestVO registrationRequestVO);

    Optional<Account> verifyEmail(UUID accountId);

    AuthenticatedAccount login(LoginRequestVO loginRequestVO);

    ConfirmRegistrationResponseVO confirmRegistration(String token);

    AuthenticatedAccount getEmailVerificationStatus(UUID accountId);

    void sendEmailNotificationToResetPassword(EmailResetPasswordRequestVO requestVO);

    Account resetPassword(ResetPasswordRequestVO requestVO);
}
