package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountNotFoundException extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(AccountNotFoundException.class);

    public AccountNotFoundException(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }
    public AccountNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
