package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ResetPasswordTokenExpiredException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(ResetPasswordTokenExpiredException.class);

    public ResetPasswordTokenExpiredException(String errorMessage) {
        super(errorMessage);
        logger.error(errorMessage);
    }
    public ResetPasswordTokenExpiredException(String errorMessage, Throwable err) {
        super(errorMessage, err);
        logger.error(errorMessage, err);
    }
}
