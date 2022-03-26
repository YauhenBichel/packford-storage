package com.ybichel.storage.account.vo;

import lombok.Data;
import java.util.UUID;

@Data
public class AccountRequestVO {
    private UUID id;
    private Boolean active;
}
