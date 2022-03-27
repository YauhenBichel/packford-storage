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

    @Query(value = "SELECT * from email_account em_acc " +
            "inner join account acc on em_acc.account_id = acc.id" +
            " where em_acc.email = :email AND " +
            "em_acc.verificated = true AND acc.active = true",
            nativeQuery = true)
    Optional<EmailAccount> findEmailAccountByEmailAndActiveTrue(@Param("email") String email);

    @Query(value = "SELECT crypt(:password, gen_salt('bf', 8))",
            nativeQuery = true)
    String generateHashedPassword(@Param("password") String password);

    Optional<EmailAccount> findEmailAccountByAccount_Id(@Param("account_id") UUID accountId);

    Optional<EmailAccount> findAccountByEmailAndVerificatedTrue(String email);

    @Query(value = "SELECT * from email_account em_acc " +
            "where em_acc.email = :email AND " +
            "crypt(:password, em_acc.password) = em_acc.password AND " +
            "em_acc.verificated = true",
            nativeQuery = true)
    Optional<EmailAccount> findEmailAccountByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    List<EmailAccount> findEmailAccountsByVerificatedFalse();
}
