package com.ybichel.storage.account.mapper;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.account.vo.AccountRequestVO;
import com.ybichel.storage.account.vo.AccountResponseVO;
import com.ybichel.storage.authorization.vo.RegistrationRequestVO;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AccountMapper {

    public Account toAccount(UUID accountId, String hashedPassWithSalt, RegistrationRequestVO registrationRequest) {
        Account account = new Account();

        account.setId(accountId);
        account.setActive(true);

        return account;
    }

    public Account toAccount(Account dbAccount, AccountRequestVO accountRequestVO) {
        return dbAccount;
    }

    public AccountResponseVO toAccountResponse(Account dbAccount) {
        AccountResponseVO responseVO = new AccountResponseVO();

        responseVO.setId(dbAccount.getId());
        responseVO.setActive(dbAccount.getActive());
        responseVO.setCreated(dbAccount.getCreated());
        responseVO.setModified(dbAccount.getModified());

        Set<String> roles = dbAccount.getRoles().stream()
                .map(StorageRole::getName)
                .collect(Collectors.toSet());
        responseVO.setRoles(roles);

        return responseVO;
    }
}
