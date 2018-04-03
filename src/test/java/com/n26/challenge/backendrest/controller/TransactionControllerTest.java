/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.controller;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.common.io.Resources;
import com.n26.challenge.backendrest.domain.Transaction;
import com.n26.challenge.backendrest.service.TransactionsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

/**
 * @author Akhtar on 02-Apr-18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    private static final String URL = "/transactions";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TransactionsServiceImpl transactionsService;

    private static String convertJsonToStringFromFile(String fileName) {
        try {
            return Resources.toString(getResource(fileName), UTF_8);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Post transaction with valid details
     *
     * @throws Exception
     */
    @Test
    public void postTransactionsWithValidTime() throws Exception {
        given(transactionsService.saveTransactions(any(Transaction.class))).willReturn(true);
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertJsonToStringFromFile("Transaction.json")))
                        .andExpect(status().isCreated());
    }

    /**
     * Post transaction with false return
     *
     * @throws Exception
     */
    @Test
    public void postTransactionsWithInValidTransaction() throws Exception {
        given(transactionsService.saveTransactions(any(Transaction.class))).willReturn(false);
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertJsonToStringFromFile("Transaction.json")))
                        .andExpect(status().isNoContent());
    }

    /**
     * Post transaction with exception thrown
     *
     * @throws Exception
     */
    @Test
    public void postTransactionsWithException() throws Exception {
        given(transactionsService.saveTransactions(any(Transaction.class))).willThrow(new IllegalArgumentException("exception occurred"));
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertJsonToStringFromFile("Transaction.json")))
                        .andExpect(status().isInternalServerError())
                        .andExpect(jsonPath("$.code").value(1000))
                        .andExpect(jsonPath("$.description").value("exception occurred"));
    }

    /**
     * Post transaction with invalid amount(less than 0.1)
     *
     * @throws Exception
     */
    @Test
    public void postTransactionsWithInValidAmount() throws Exception {
        given(transactionsService.saveTransactions(any(Transaction.class))).willReturn(true);
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertJsonToStringFromFile("InValidAmountTransaction.json")))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(3000))
                        .andExpect(jsonPath("$.description").value("amount must be greater than or equal to 0.01"));
    }

    /**
     * post transaction with invalid time stamp in milliseconds(less than 0)
     *
     * @throws Exception
     */
    @Test
    public void postTransactionsWithInValidTime() throws Exception {
        given(transactionsService.saveTransactions(any(Transaction.class))).willReturn(true);
        mockMvc.perform(post(URL)
                        .contentType(APPLICATION_JSON)
                        .content(convertJsonToStringFromFile("InValidTimeTransaction.json")))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(3000))
                        .andExpect(jsonPath("$.description").value("timeStamp must be greater than or equal to 0"));
    }
}