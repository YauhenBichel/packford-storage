package com.ybichel.storage.account.service;

import com.google.api.client.util.Sets;
import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.account.mapper.AccountMapper;
import com.ybichel.storage.account.repository.AccountRepository;
import com.ybichel.storage.account.vo.AccountRequestVO;
import com.ybichel.storage.authorization.repository.EmailAccountRepository;
import com.ybichel.storage.common.exception.AccountNotFoundException;
import com.ybichel.storage.security.Constants;
import com.ybichel.storage.security.entity.StorageRole;
import com.ybichel.storage.security.repository.StorageRoleRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.List;

@Service
public class AccountService implements IAccountService {

    private static final Logger logger = LogManager.getLogger(AccountService.class);

    private final AccountRepository accountRepository;
    private final EmailAccountRepository emailAccountRepository;
    private final StorageRoleRepository roleRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository,
                          EmailAccountRepository emailAccountRepository,
                          StorageRoleRepository roleRepository,
                          AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.emailAccountRepository = emailAccountRepository;
        this.roleRepository = roleRepository;
        this.accountMapper = accountMapper;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Page<Account> getAccounts(Specification<Account> spec, Pageable pageable) {
        return accountRepository.findAll(spec, pageable);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Account> findActiveAccount(UUID accountId) {
        return accountRepository.findAccountByIdAndActiveTrue(accountId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<Account> findDeactivatedAccounts() {
        return accountRepository.findAccountsByActiveFalse();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Account createAccount(UUID accountId) {
        Account account = new Account();
        account.setId(accountId);
        account.setActive(true);

        Optional<StorageRole> optRole = roleRepository.findStorageRoleByNameEquals(Constants.AUTHORITY_STORAGE_USER);
        optRole.ifPresent(role -> {
            Set<StorageRole> roles = Sets.newHashSet();
            roles.add(role);

            account.setRoles(roles);
        });

        return accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Optional<Account> updateAccount(UUID accountId, AccountRequestVO accountRequestVO) {
        final Optional<Account> optAccount = this.findActiveAccount(accountId);

        if (optAccount.isEmpty()) {
            return Optional.empty();
        }

        Account dbAccount = accountMapper.toAccount(optAccount.get(), accountRequestVO);
        accountRepository.save(dbAccount);

        return Optional.of(dbAccount);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Account updateAccount(Account updatedEntity) {
        final Optional<Account> optAccount = this.findActiveAccount(updatedEntity.getId());

        if (optAccount.isEmpty()) {
            logger.info("Active account id = {} not found", updatedEntity.getId());
            throw new AccountNotFoundException("account not found");
        }

        this.accountRepository.save(updatedEntity);

        return updatedEntity;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deactivateAccount(UUID accountId) {

        Optional<Account> optDbAccount = accountRepository.findAccountByIdAndActiveTrue(accountId);
        if (optDbAccount.isPresent()) {

            logger.warn("deactivate account id = {}", accountId);

            Account dbAccount = optDbAccount.get();
            dbAccount.setActive(false);

            accountRepository.save(dbAccount);
        }
    }
}
