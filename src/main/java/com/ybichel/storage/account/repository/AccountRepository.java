package com.ybichel.storage.account.repository;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface AccountRepository extends SpecificationPagingAndSortingRepository<Account, UUID> {

    Optional<Account> findAccountByIdAndActiveTrue(UUID id);

    List<Account> findAccountsByActiveFalse();
}
