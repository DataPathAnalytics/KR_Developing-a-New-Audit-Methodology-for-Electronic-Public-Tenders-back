package com.datapath.kg.risks.api.exception;

public abstract class ExceptionAbs extends RuntimeException {
    private String code;

    ExceptionAbs(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
