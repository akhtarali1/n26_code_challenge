/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import com.n26.challenge.backendrest.domain.Error;
import com.n26.challenge.backendrest.domain.N26Exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Akhtar on 02-Apr-18.
 */
@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    /**
     * @param exception {@link N26Exception}
     * @return error with status code
     */
    @ExceptionHandler(value = N26Exception.class)
    @ResponseStatus(BAD_REQUEST)
    public Error handleN26Exception(N26Exception exception) {
        log.error("Unexpected exception thrown from service, {}", exception.getMessage());
        return exception.getError();
    }

    /**
     * Catches all runtime exceptions and converts into error
     *
     * @param exception all runtime exceptions
     * @return error with status code
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public Error handleException(Exception exception) {
        log.error("Unexpected exception thrown from service, {}", exception.getMessage());
        return getError(exception.getMessage(), 1000);
    }

    /**
     * @param description for error
     * @param code        for error
     * @return error with code and description
     */
    private Error getError(String description, Integer code) {
        Error error = new Error();
        error.setCode(code);
        error.setDescription(description);
        return error;
    }
}