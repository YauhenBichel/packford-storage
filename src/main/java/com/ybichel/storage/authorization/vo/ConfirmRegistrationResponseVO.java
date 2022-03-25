package com.ybichel.storage.authorization.vo;

import com.ybichel.storage.authorization.model.VerificationTokenStatus;
import lombok.Data;

@Data
public class ConfirmRegistrationResponseVO {
    private VerificationTokenStatus tokenStatus;
    private String message;
}
