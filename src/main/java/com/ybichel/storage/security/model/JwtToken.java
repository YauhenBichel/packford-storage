package com.ybichel.storage.security.model;

import lombok.Data;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.UUID;

@Data
public class JwtToken {
    private UUID accountId;
    private String email;
    private List<SimpleGrantedAuthority> authorities;
}
