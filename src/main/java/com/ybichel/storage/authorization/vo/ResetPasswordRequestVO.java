package com.ybichel.storage.authorization.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ResetPasswordRequestVO {

    @NotNull
    private UUID token;
    @NotBlank
    private String password;
    @NotBlank
    private String clientApp;
}
