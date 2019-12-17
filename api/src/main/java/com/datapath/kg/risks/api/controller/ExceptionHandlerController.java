package com.datapath.kg.risks.api.controller;

import com.datapath.kg.risks.api.exception.*;
import com.datapath.kg.risks.api.response.exception.ExceptionResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {
            ChecklistSavingException.class,
            ChecklistExportException.class,
            ChecklistDeleteException.class,
            PrioritizationExportException.class,
            UpdatePasswordException.class,
            ResetPasswordException.class,
            AuditorRegisterException.class})
    public ResponseEntity<ExceptionResponse> exception(ExceptionAbs ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage(), ex.getCode());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
