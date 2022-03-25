package com.ybichel.storage.common.search.rsql;

/**
 * This exception is thrown when illegal field name was passed to rsql request
 */
public class IllegalRsqlArgumentException extends RuntimeException {
    public IllegalRsqlArgumentException() {
        super();
    }

    public IllegalRsqlArgumentException(String message) {
        super(message);
    }

    public IllegalRsqlArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalRsqlArgumentException(Throwable cause) {
        super(cause);
    }
}
