package com.ybichel.storage.authorization.vo;

import lombok.Data;
import java.util.UUID;

@Data
public class ResetPasswordResponseVO {
    private UUID id;
    private String email;
    private Boolean verificated;
    private Boolean active;
}
