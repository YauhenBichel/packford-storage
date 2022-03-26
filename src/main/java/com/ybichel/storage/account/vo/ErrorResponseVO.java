package com.ybichel.storage.account.vo;

import com.ybichel.storage.account.entity.Account;
import lombok.Data;

@Data
public class ErrorResponseVO {
    private Account account;
    private Integer errorCode;
}
