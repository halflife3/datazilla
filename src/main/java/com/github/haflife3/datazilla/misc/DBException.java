package com.github.haflife3.datazilla.misc;

/**
 * @author halflife3
 * @date 2019/9/21
 */
public class DBException extends RuntimeException{
    
    public DBException() {
        super();
    }

    public DBException(String message) {
        super(message);
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(Throwable cause) {
        super(cause);
    }

    protected DBException(String message, Throwable cause,boolean enableSuppression,boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
