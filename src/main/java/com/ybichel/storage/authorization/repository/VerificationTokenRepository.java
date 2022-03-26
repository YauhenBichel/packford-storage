package com.ybichel.storage.authorization.repository;

import com.ybichel.storage.authorization.entity.VerificationToken;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends SpecificationPagingAndSortingRepository<VerificationToken, UUID> {
    Optional<VerificationToken> findByToken(String token);

    Optional<VerificationToken> findFirstByEmailAccountId(UUID emailAccountId);
}
