package com.ybichel.storage.authorization.repository;

import com.ybichel.storage.account.entity.EmailAccount;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailAccountRepository extends SpecificationPagingAndSortingRepository<EmailAccount, UUID> {
}
