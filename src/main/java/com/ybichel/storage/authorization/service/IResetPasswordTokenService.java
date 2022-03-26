package com.ybichel.storage.authorization.service;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.entity.ResetPasswordToken;

import java.util.Optional;
import java.util.UUID;

public interface IResetPasswordTokenService {
    ResetPasswordToken createVerificationToken(UUID resetPasswordTokenId, String token, EmailAccount emailAccount);

    Optional<ResetPasswordToken> findByToken(String token);

    void deleteByEmailAccountId(UUID accountId);
}
