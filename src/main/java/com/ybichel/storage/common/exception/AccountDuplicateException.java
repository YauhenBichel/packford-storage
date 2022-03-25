package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountDuplicateException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(AccountDuplicateException.class);

    public AccountDuplicateException(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }
    public AccountDuplicateException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
