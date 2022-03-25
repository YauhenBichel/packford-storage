package com.ybichel.storage.authorization.mapper;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.model.AuthenticatedAccount;
import com.ybichel.storage.authorization.vo.LoginResponseVO;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthenticatedAccountMapper {

    public AuthenticatedAccount toLoginModel(Account account, String jwtToken) {
        AuthenticatedAccount authenticatedAccount = new AuthenticatedAccount();

        authenticatedAccount.setAccount(account);
        authenticatedAccount.setJwtToken(jwtToken);

        return authenticatedAccount;
    }
    public AuthenticatedAccount toLoginModel(Optional<Account> account, String jwtToken) {
        AuthenticatedAccount authenticatedAccount = new AuthenticatedAccount();

        if(account.isPresent()) {
            authenticatedAccount.setAccount(account.get());
            authenticatedAccount.setJwtToken(jwtToken);
        } else {
            authenticatedAccount.setAccount(null);
        }

        return authenticatedAccount;
    }

    public LoginResponseVO toLoginResponseVO(AuthenticatedAccount authenticatedAccount) {
        LoginResponseVO responseVO = new LoginResponseVO();
        final Account dbAccount = authenticatedAccount.getAccount();

        responseVO.setId(dbAccount.getId());
        responseVO.setFirstName(dbAccount.getFirstName());
        responseVO.setEmail(dbAccount.getEmail());
        responseVO.setActive(dbAccount.getActive());
        responseVO.setVerificated(dbAccount.getVerificated());
        responseVO.setJwtToken(authenticatedAccount.getJwtToken());

        Set<String> roles = dbAccount.getRoles().stream()
                .map(StorageRole::getName)
                .collect(Collectors.toSet());

        responseVO.setRoles(roles);

        return responseVO;
    }
}
