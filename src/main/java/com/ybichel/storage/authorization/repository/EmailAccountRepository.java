package com.ybichel.storage.authorization.repository;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.common.repository.SpecificationPagingAndSortingRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailAccountRepository extends SpecificationPagingAndSortingRepository<EmailAccount, UUID> {
    Optional<EmailAccount> findAccountByEmailAndActiveTrue(String email);

    @Query( value = "SELECT crypt(:password, gen_salt('bf', 8))",
            nativeQuery = true )
    String generateHashedPassword(@Param("password") String password);

    Optional<EmailAccount> findEmailAccountByAccount_Id(@Param("account_id") UUID accountId);

    Optional<EmailAccount> findAccountByEmailAndVerificatedTrue(String email);

    @Query( value = "SELECT * from email_account acc " +
            "where acc.email = :email AND " +
            "crypt(:password, acc.password) = acc.password AND " +
            "acc.verificated = true",
            nativeQuery = true )
    Optional<EmailAccount> findEmailAccountByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    List<EmailAccount> findEmailAccountsByVerificatedFalse();
}
