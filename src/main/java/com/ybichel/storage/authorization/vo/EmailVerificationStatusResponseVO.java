package com.ybichel.storage.authorization.vo;

import lombok.Data;

import java.util.UUID;

@Data
public class EmailVerificationStatusResponseVO {
    private Boolean verificated;
    private UUID accountId;
    private String jwtToken;
}
