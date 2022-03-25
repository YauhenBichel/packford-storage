package com.ybichel.storage.account.repository;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

@Repository
public interface AccountRepository extends SpecificationPagingAndSortingRepository<Account, UUID> {
    @Query( value = "SELECT crypt(:password, gen_salt('bf', 8))",
            nativeQuery = true )
    String generateHashedPassword(@Param("password") String password);

    Optional<Account> findAccountByIdAndActiveTrue(UUID id);

    Optional<Account> findAccountByEmailAndActiveTrue(String email);

    @Query( value = "SELECT * from account acc " +
            "where acc.email = :email AND " +
            "crypt(:password, acc.password) = acc.password AND " +
            "acc.active = true",
            nativeQuery = true )
    Optional<Account> findAccountByEmailAndPasswordAndActiveTrue(@Param("email") String email, @Param("password") String password);

    Optional<Account> findAccountByBodyAppreciationId(UUID bodyAppreciationId);

    Optional<Account> findAccountByAppleAccountId(UUID appleAccountId);

    //Optional<Account> findAccountByFeedbackId(UUID feedbackId);

    Optional<Account> findAccountByEmailAndActiveTrueAndVerificatedTrue(String email);

    @Query( value = "SELECT * from account acc " +
            "where acc.email = :email AND " +
            "crypt(:password, acc.password) = acc.password AND " +
            "acc.active = true AND " +
            "acc.verificated = true",
            nativeQuery = true )
    Optional<Account> findAccountByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    List<Account> findAccountsByActiveFalse();

    List<Account> findAccountsByVerificatedFalse();
}
