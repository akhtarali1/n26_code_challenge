/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.n26.challenge.backendrest.domain.N26Exception;
import com.n26.challenge.backendrest.domain.Transaction;
import com.n26.challenge.backendrest.service.TransactionsServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Transactions controller
 *
 * @author Akhtar on 02-Apr-18.
 */
@RestController
@RequestMapping(value = "/transactions", consumes = APPLICATION_JSON_VALUE)
public class TransactionController {

    private final TransactionsServiceImpl transactionsService;

    /**
     * @param transactionsService {@link TransactionsServiceImpl}
     */
    public TransactionController(TransactionsServiceImpl transactionsService) {
        this.transactionsService = transactionsService;
    }

    /**
     * @param transaction   to be validated and saved
     * @param bindingResult consists of domain fields validation errors
     * @return status code 204 for expired transactions, 200 for saved transaction
     */
    @RequestMapping(method = POST)
    public ResponseEntity postTransactions(@RequestBody @Valid Transaction transaction, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            String errorDescription = bindingResult.getFieldError().getField() + " " + bindingResult.getFieldError().getDefaultMessage();
            throw new N26Exception(errorDescription, 3000);
        }

        if (transactionsService.saveTransactions(transaction)) {
            return new ResponseEntity(CREATED);
        } else {
            return new ResponseEntity(NO_CONTENT);
        }
    }
}