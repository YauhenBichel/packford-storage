package com.ybichel.storage.account.vo;

import lombok.Data;
import java.util.UUID;

@Data
public class AccountRequestVO {
    private UUID id;
    private String firstName;
    private String email;
    private Boolean active;
    private Boolean verificated;
}
