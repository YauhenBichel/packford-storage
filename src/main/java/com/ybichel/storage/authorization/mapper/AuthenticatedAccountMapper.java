package com.ybichel.storage.authorization.mapper;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.model.AuthenticatedAccount;
import com.ybichel.storage.authorization.vo.LoginResponseVO;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthenticatedAccountMapper {

    public AuthenticatedAccount toLoginModel(EmailAccount emailAccount, String jwtToken) {
        AuthenticatedAccount authenticatedAccount = new AuthenticatedAccount();

        authenticatedAccount.setEmailAccountId(emailAccount.getId());
        authenticatedAccount.setAccountId(emailAccount.getAccount().getId());
        authenticatedAccount.setActive(emailAccount.getAccount().getActive());
        authenticatedAccount.setJwtToken(jwtToken);
        authenticatedAccount.setEmail(emailAccount.getEmail());
        authenticatedAccount.setPassword(emailAccount.getPassword());
        authenticatedAccount.setVerificated(emailAccount.getVerificated());
        authenticatedAccount.setRoles(emailAccount.getAccount().getRoles());

        return authenticatedAccount;
    }

    public AuthenticatedAccount toLoginModel(Optional<EmailAccount> optEmailAccount, String jwtToken) {
        AuthenticatedAccount authenticatedAccount = new AuthenticatedAccount();

        if (optEmailAccount.isPresent()) {
            return toLoginModel(optEmailAccount.get(), jwtToken);
        }

        return authenticatedAccount;
    }

    public LoginResponseVO toLoginResponseVO(AuthenticatedAccount authenticatedAccount) {
        LoginResponseVO responseVO = new LoginResponseVO();

        responseVO.setId(authenticatedAccount.getAccountId());
        responseVO.setActive(authenticatedAccount.getActive());
        responseVO.setJwtToken(authenticatedAccount.getJwtToken());

        Set<String> roles = authenticatedAccount.getRoles().stream()
                .map(StorageRole::getName)
                .collect(Collectors.toSet());

        responseVO.setRoles(roles);

        return responseVO;
    }
}
