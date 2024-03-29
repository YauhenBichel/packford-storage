package com.ybichel.storage.authorization.service;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.entity.VerificationToken;

import java.util.Optional;
import java.util.UUID;

public interface IVerificationTokenService {
    VerificationToken createVerificationToken(UUID verificationTokenId, String token, EmailAccount emailAccount);

    Optional<VerificationToken> findByToken(String token);

    void deleteByAccountId(UUID accountId);
}
