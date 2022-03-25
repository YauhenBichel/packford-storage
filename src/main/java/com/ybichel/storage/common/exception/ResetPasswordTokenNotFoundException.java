package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetPasswordTokenNotFoundException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(ResetPasswordTokenNotFoundException.class);

    public ResetPasswordTokenNotFoundException(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }
    public ResetPasswordTokenNotFoundException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
