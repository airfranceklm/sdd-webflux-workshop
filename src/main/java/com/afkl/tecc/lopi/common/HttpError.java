package com.afkl.tecc.lopi.common;

public class HttpError {

    private final int code;
    private final String message;

    public HttpError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
