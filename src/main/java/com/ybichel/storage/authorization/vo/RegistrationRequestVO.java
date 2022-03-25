package com.ybichel.storage.authorization.vo;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class RegistrationRequestVO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
