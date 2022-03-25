package com.ybichel.storage.authorization.repository;

import com.ybichel.storage.authorization.entity.ResetPasswordToken;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ResetPasswordTokenRepository extends SpecificationPagingAndSortingRepository<ResetPasswordToken, UUID> {
    Optional<ResetPasswordToken> findByToken(String token);
    Optional<ResetPasswordToken> findFirstByAccount_Id(UUID accountId);
}
