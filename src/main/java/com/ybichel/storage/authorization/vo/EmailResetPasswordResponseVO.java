package com.ybichel.storage.authorization.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailResetPasswordResponseVO {
    @NotBlank
    private String email;
    @NotBlank
    private String clientApp;
}
