package com.ybichel.storage.authorization.mapper;

import com.ybichel.storage.authorization.entity.EmailAccount;
import com.ybichel.storage.authorization.vo.RegistrationResponseVO;
import com.ybichel.storage.security.entity.StorageRole;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class AuthMapper {
    public RegistrationResponseVO toRegistrationResponseVO(EmailAccount dbEmailAccount) {
        RegistrationResponseVO responseVO = new RegistrationResponseVO();

        responseVO.setId(dbEmailAccount.getId());
        responseVO.setEmail(dbEmailAccount.getEmail());
        responseVO.setVerificated(dbEmailAccount.getVerificated());
        responseVO.setActive(dbEmailAccount.getAccount().getActive());

        Set<String> roles = dbEmailAccount.getAccount().getRoles().stream()
                .map(StorageRole::getName)
                .collect(Collectors.toSet());

        responseVO.setRoles(roles);

        return responseVO;
    }
}
