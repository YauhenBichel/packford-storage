package com.ybichel.storage.authorization.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class EmailResetPasswordRequestVO {
    @NotBlank
    private String email;
}
