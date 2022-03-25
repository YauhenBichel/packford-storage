package com.ybichel.storage.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
public class AccountNotVerificatedException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(AccountNotVerificatedException.class);

    private UUID accountId;

    public AccountNotVerificatedException(String errorMessage, UUID accountId) {
        super(errorMessage);
        this.accountId = accountId;
        logger.error(errorMessage);
    }
    public AccountNotVerificatedException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
