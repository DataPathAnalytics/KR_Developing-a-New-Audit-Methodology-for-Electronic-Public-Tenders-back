package com.datapath.kg.risks.api.response.exception;

import lombok.Data;

@Data
public class ExceptionResponse {
    private String message;
    private String code;

    public ExceptionResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
