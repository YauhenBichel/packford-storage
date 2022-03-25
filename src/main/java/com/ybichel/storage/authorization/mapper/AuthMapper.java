package com.ybichel.storage.authorization.mapper;

import com.ybichel.storage.account.entity.Account;
import com.ybichel.storage.authorization.vo.RegistrationResponseVO;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {
    public RegistrationResponseVO toRegistrationResponseVO(Account dbAccount) {
        RegistrationResponseVO responseVO = new RegistrationResponseVO();

        responseVO.setId(dbAccount.getId());
        responseVO.setFirstName(dbAccount.getFirstName());
        responseVO.setEmail(dbAccount.getEmail());
        responseVO.setActive(dbAccount.getActive());
        responseVO.setVerificated(dbAccount.getVerificated());

        Set<String> roles = dbAccount.getRoles().stream()
                .map(StorageRole::getName)
                .collect(Collectors.toSet());

        responseVO.setRoles(roles);

        return responseVO;
    }
}
