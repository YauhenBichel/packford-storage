package com.ybichel.storage.authorization.service;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.entity.VerificationToken;
import com.ybichel.storage.authorization.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService implements IVerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public VerificationToken createVerificationToken(UUID verificationTokenId, String token, EmailAccount emailAccount) {
        VerificationToken verificationToken = new VerificationToken(verificationTokenId,
                token,
                emailAccount);

        return verificationTokenRepository.save(verificationToken);
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public Optional<VerificationToken> findByToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Transactional( propagation = Propagation.REQUIRED )
    public void deleteByAccountId(UUID accountId) {
        Optional<VerificationToken> optDbVerificationToken = verificationTokenRepository.findFirstByAccount_Id(accountId);
        optDbVerificationToken.ifPresent(verificationTokenRepository::delete);
    }
}
