package com.arun.a85mm.exception;

/**
 * Created by wy on 2017/7/5.
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;
    public Throwable throwable;

    public ServerException(int code, String message) {
        this.code = code;
        this.message = message;
        this.throwable = getCause();
    }
}
