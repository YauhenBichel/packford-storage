package com.ybichel.storage.authorization.service;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.entity.ResetPasswordToken;
import com.ybichel.storage.authorization.repository.ResetPasswordTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordTokenService implements IResetPasswordTokenService {

    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public ResetPasswordToken createVerificationToken(UUID resetPasswordTokenId, String token, EmailAccount emailAccount) {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(resetPasswordTokenId,
                token,
                emailAccount);

        return resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public Optional<ResetPasswordToken> findByToken(String token) {
        return resetPasswordTokenRepository.findByToken(token);
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public void deleteByAccountId(UUID accountId) {
        Optional<ResetPasswordToken> optDbVerificationToken = resetPasswordTokenRepository.findFirstByAccount_Id(accountId);
        optDbVerificationToken.ifPresent(resetPasswordTokenRepository::delete);
    }
}
