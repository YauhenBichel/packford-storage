package com.ybichel.storage.authorization.vo;

import com.ybichel.storage.account.entity.Account;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetPasswordResponseVO {
    private Account account;
}
