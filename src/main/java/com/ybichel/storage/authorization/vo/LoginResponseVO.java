package com.ybichel.storage.authorization.vo;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class LoginResponseVO {
    private UUID id;
    private String email;
    private Boolean active;
    private Boolean verificated;
    private String jwtToken;
    private Set<String> roles;
}
