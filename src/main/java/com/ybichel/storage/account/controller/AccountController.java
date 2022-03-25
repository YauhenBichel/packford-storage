package com.ybichel.storage.account.controller;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.account.mapper.AccountMapper;
import com.ybichel.storage.account.service.IAccountService;
import com.ybichel.storage.account.vo.AccountRequestVO;
import com.ybichel.storage.account.vo.AccountResponseVO;
import com.ybichel.storage.common.exception.AccountNotFoundException;
import com.ybichel.storage.common.search.SpecificationBuilder;
import com.ybichel.storage.security.model.JwtToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account")
public class AccountController {

    //logs
    private static final Logger logger = LogManager.getLogger(AccountController.class);

    private final IAccountService accountService;
    private final AccountMapper accountMapper;

    public AccountController(IAccountService accountService,
                             AccountMapper accountMapper) {
        this.accountService = accountService;
        this.accountMapper = accountMapper;
    }

    @GetMapping("/{id}")
    @CrossOrigin(origins = "*")
    @PreAuthorize("hasAnyAuthority('STORAGE_USER', 'STORAGE_ADMIN')")
    public ResponseEntity<AccountResponseVO> getById(@PathVariable("id") final UUID id,
                                                     Authentication authentication) {

        final JwtToken jwtToken = (JwtToken) authentication.getPrincipal();
        
        if (jwtToken.getAccountId().equals(id)) {
            Optional<Account> optAccount = accountService.findActiveAccount(id);
            if (optAccount.isEmpty()) {

                logger.info("Active account id = {} not found", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            AccountResponseVO responseVO = accountMapper.toAccountResponse(optAccount.get());
            return new ResponseEntity<>(responseVO, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/{id}")
    @CrossOrigin(origins = "*")
    @PreAuthorize("hasAnyAuthority('STORAGE_USER', 'STORAGE_ADMIN')")
    public ResponseEntity<AccountResponseVO> update(@PathVariable("id") final UUID id,
                                 @Valid @RequestBody AccountRequestVO accountRequestVO,
                                          Authentication authentication) {

        final JwtToken jwtToken = (JwtToken) authentication.getPrincipal();

        if (jwtToken.getAccountId().equals(id)) {
            Optional<Account> optUpdateAccount = this.accountService.updateAccount(id, accountRequestVO);
            if (optUpdateAccount.isEmpty()) {
                logger.info("Active account id = {} not found", id);
                throw new AccountNotFoundException("account is not found");
            }

            AccountResponseVO responseVO = accountMapper.toAccountResponse(optUpdateAccount.get());
            return new ResponseEntity<>(responseVO, HttpStatus.OK);
        }

        throw new AccountNotFoundException("wrong account id");
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    @PreAuthorize("hasAnyAuthority('STORAGE_ADMIN')")
    public ResponseEntity<Page<AccountResponseVO>> getAccounts(@RequestParam( name = "q", required = false ) final String predicate,
                                                     @PageableDefault Pageable pageable) {

        Specification<Account> spec = SpecificationBuilder.build(Account.class, predicate, null);
        Page<AccountResponseVO> responses = accountService.getAccounts(spec, pageable)
                .map(accountMapper::toAccountResponse);

        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin(origins = "*")
    @PreAuthorize("hasAnyAuthority('STORAGE_USER', 'STORAGE_ADMIN', 'STORAGE_ADMIN', 'MOLECARE_SYSTEM')")
    public ResponseEntity deleteAccount(@PathVariable("id") final UUID id, Authentication authentication) {
        final JwtToken jwtToken = (JwtToken) authentication.getPrincipal();

        if (jwtToken.getAccountId().equals(id)) {
            logger.info("Deactive account id = {}", id);
            accountService.deactivateAccount(id);

            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
