package com.ybichel.storage.account.vo;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
public class AccountResponseVO {
    private UUID id;
    private Boolean active;
    private Set<String> roles;
    private LocalDateTime created;
    private LocalDateTime modified;
}
