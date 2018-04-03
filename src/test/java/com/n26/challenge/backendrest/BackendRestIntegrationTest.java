/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.challenge.backendrest.domain.Transaction;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author Akhtar on 02-Apr-18.
 */
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest(classes = BackendRestApplication.class)
public class BackendRestIntegrationTest {

    private static final String STATISTICS = "/statistics";
    private static final String TRANSACTIONS = "/transactions";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper mapper;

    /**
     * Get statistics when no transactions present in DB
     *
     * @throws Exception
     */
    @Test
    public void test0GetStatisticsINEmptyDB() throws Exception {
        mvc.perform(get(STATISTICS)
                        .contentType(APPLICATION_JSON_VALUE))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(2000))
                        .andExpect(jsonPath("$.description").value("No transactions happened in last 60 seconds"));
    }

    /**
     * Post transactions with valid data
     *
     * @throws Exception
     */
    @Test
    public void test1PostTransactions() throws Exception {
        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(11.5D, System.currentTimeMillis()))))
                        .andExpect(status().isCreated());

        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(12.5D, System.currentTimeMillis() - 100))))
                        .andExpect(status().isCreated());

        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(14.5D, System.currentTimeMillis() - 6000))))
                        .andExpect(status().isCreated());

        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(15.5D, System.currentTimeMillis() - 49999))))
                        .andExpect(status().isCreated());
    }

    /**
     * Post transaction with expired timestamp
     *
     * @throws Exception
     */
    @Test
    public void test1PostInvalidTransactions() throws Exception {
        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(11.5D, System.currentTimeMillis() - 61111))))
                        .andExpect(status().isNoContent());

        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(12.5D, System.currentTimeMillis() - 77777))))
                        .andExpect(status().isNoContent());

    }

    /**
     * Get statistics of saved transaction in testcase-1
     *
     * @throws Exception
     */
    @Test
    public void test2GetStatistics() throws Exception {
        mvc.perform(get(STATISTICS)
                        .contentType(APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sum").value(54))
                        .andExpect(jsonPath("$.avg").value(13.5))
                        .andExpect(jsonPath("$.max").value(15.5))
                        .andExpect(jsonPath("$.min").value(11.5))
                        .andExpect(jsonPath("$.count").value(4));
    }

    /**
     * Post transaction with invalid amount or timestamp
     *
     * @throws Exception
     */
    @Test
    public void test3PostInvalidDataInTransactions() throws Exception {
        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(0, System.currentTimeMillis()))))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(3000))
                        .andExpect(jsonPath("$.description").value("amount must be greater than or equal to 0.01"));

        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(12.5D, -1))))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(3000))
                        .andExpect(jsonPath("$.description").value("timeStamp must be greater than or equal to 0"));
    }

    /**
     * Update DB with new transactions and get statistics of updated DB
     *
     * @throws Exception
     */
    @Test
    public void test4PostTransactionAndGetStatistics() throws Exception {
        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(20.5D, System.currentTimeMillis()))))
                        .andExpect(status().isCreated());
        mvc.perform(post(TRANSACTIONS)
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(buildTransaction(9.5D, System.currentTimeMillis()))))
                        .andExpect(status().isCreated());

        mvc.perform(get(STATISTICS)
                        .contentType(APPLICATION_JSON_VALUE))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sum").value(84))
                        .andExpect(jsonPath("$.avg").value(14))
                        .andExpect(jsonPath("$.max").value(20.5))
                        .andExpect(jsonPath("$.min").value(9.5))
                        .andExpect(jsonPath("$.count").value(6));
    }

    private Transaction buildTransaction(double amount, long timeStamp) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimeStamp(timeStamp);
        return transaction;
    }
}
