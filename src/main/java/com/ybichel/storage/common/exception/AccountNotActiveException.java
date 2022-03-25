package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountNotActiveException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(AccountNotActiveException.class);

    public AccountNotActiveException(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }
    public AccountNotActiveException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
