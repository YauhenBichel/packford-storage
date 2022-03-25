package com.ybichel.storage.authorization.model;

import com.ybichel.storage.account.entity.Account;
import lombok.Data;

@Data
public class AuthenticatedAccount {
    private Account account;
    private String jwtToken;
}
