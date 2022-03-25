package com.ybichel.storage.account.service;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.account.vo.AccountRequestVO;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAccountService {
    Page<Account> getAccounts(Specification<Account> spec, Pageable pageable);
    Optional<Account> findActiveAccount(UUID accountId);
    Optional<Account> findActiveAccount(String email);
    Optional<Account> findActiveAccountByAppleId(UUID appleId);
    List<Account> findDeactivatedAccounts();
    List<Account> findUnverifiedAccounts();
    Optional<Account> findActiveAndVerificatedAccount(String email);
    Optional<Account> findActiveAccountByEmailAndPassword(String email, String password);
    Account createAccount(UUID accountId, String hashedPassWithSalt, RegistrationRequestVO registrationRequestVO);
    Optional<Account> updateAccount(UUID accountId, AccountRequestVO accountRequestVO);
    Account updateAccount(Account updatedEntity);
    void saveAccount(Account account);
    void deactivateAccount(UUID accountId);
    String generateHash(String password);
}
