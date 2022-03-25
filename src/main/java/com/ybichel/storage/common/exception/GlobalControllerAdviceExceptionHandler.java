package com.ybichel.storage.common.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalControllerAdviceExceptionHandler.class);

    @ExceptionHandler(value = AccountDuplicateException.class)
    public final ResponseEntity<Object> handleException(AccountDuplicateException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ErrorCode.ACCOUNT_DUPLICATE);
        errorResponse.setMessage("The same verificated account exists.");

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = AccountNotVerificatedException.class)
    public final ResponseEntity<Object> handleException(AccountNotVerificatedException ex, WebRequest request) {

        ErrorNotVerificatedResponse errorResponse = new ErrorNotVerificatedResponse();
        errorResponse.setError(ErrorCode.ACCOUNT_NOT_VERIFICATED);
        errorResponse.setMessage("Account is not verificated.");
        errorResponse.setAccountId(ex.getAccountId());

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = AccountNotActiveException.class)
    public final ResponseEntity<Object> handleException(AccountNotActiveException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ErrorCode.ACCOUNT_NOT_ACTIVE);
        errorResponse.setMessage("Account is not active.");

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = AccountNotFoundException.class)
    public final ResponseEntity<Object> handleException(AccountNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ErrorCode.ACCOUNT_NOT_FOUND);
        errorResponse.setMessage("Account is not found.");

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = ResetPasswordTokenNotFoundException.class)
    public final ResponseEntity<Object> handleException(ResetPasswordTokenNotFoundException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ErrorCode.RESET_PASSWORD_TOKEN_NOT_FOUND);
        errorResponse.setMessage("bad token");

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ResetPasswordTokenExpiredException.class)
    public final ResponseEntity<Object> handleException(ResetPasswordTokenExpiredException ex, WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError(ErrorCode.RESET_PASSWORD_TOKEN_EXPIRED);
        errorResponse.setMessage("token is expired");

        logger.error(ex);

        return handleExceptionInternal(ex, errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
