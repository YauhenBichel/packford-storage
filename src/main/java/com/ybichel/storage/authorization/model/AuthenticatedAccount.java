package com.ybichel.storage.authorization.model;

import com.ybichel.storage.security.entity.StorageRole;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class AuthenticatedAccount {
    private UUID accountId;
    private Boolean active;
    private UUID emailAccountId;
    private Set<StorageRole> roles;
    private String jwtToken;
    private String email;
    private String password;
    private Boolean verificated;
}
