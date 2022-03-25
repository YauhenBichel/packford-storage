package com.ybichel.storage.account.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class AccountResponseVO {
    private UUID id;
    private String firstName;
    private String email;
    private Boolean active;
    private Boolean verificated;
    private LocalDateTime created;
    private LocalDateTime modified;
    private Set<String> roles;
}
