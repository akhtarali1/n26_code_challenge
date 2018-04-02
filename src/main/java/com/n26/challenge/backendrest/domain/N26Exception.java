/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.domain;

/**
 * @author Akhtar on 02-Apr-18.
 */
public class N26Exception extends RuntimeException {
    private final Error error;

    /**
     * Default Constructor for server Start-up
     */
    public N26Exception() {
        error = new Error();
    }

    /**
     * @param message error message
     * @param code error code
     */
    public N26Exception(String message, Integer code) {
        super(message);
        error = new Error();
        error.setCode(code);
        error.setDescription(message);
    }

    /**
     * Gets errors.
     *
     * @return Value of errors.
     */
    public Error getError() {
        return error;
    }
}