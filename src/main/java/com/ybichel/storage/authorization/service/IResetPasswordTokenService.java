package com.ybichel.storage.authorization.service;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.entity.ResetPasswordToken;

import java.util.Optional;
import java.util.UUID;

public interface IResetPasswordTokenService {
    ResetPasswordToken createVerificationToken(UUID resetPasswordTokenId, String token, Account account);
    Optional<ResetPasswordToken> findByToken(String token);
    void deleteByAccountId(UUID accountId);
}
