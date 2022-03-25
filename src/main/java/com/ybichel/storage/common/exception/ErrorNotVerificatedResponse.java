package com.ybichel.storage.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class ErrorNotVerificatedResponse extends ErrorResponse {
    private UUID accountId;
}
