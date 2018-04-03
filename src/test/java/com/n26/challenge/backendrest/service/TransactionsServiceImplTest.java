/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import com.n26.challenge.backendrest.domain.Transaction;
import com.n26.challenge.backendrest.entity.TransactionEntity;
import com.n26.challenge.backendrest.repository.TransactionsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author Akhtar on 03-Apr-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class TransactionsServiceImplTest {

    @InjectMocks
    private TransactionsServiceImpl transactionsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Test
    public void saveTransactions() {
        when(transactionsRepository.save(ArgumentMatchers.any(TransactionEntity.class))).thenReturn(new TransactionEntity());
        assertTrue("Expected TRUE is not returned",
                        transactionsService.saveTransactions(buildTransaction(11.2D, System.currentTimeMillis() - 1000)));
    }

    @Test
    public void saveTransactionsWithInvalidTime() {
        assertFalse("Expected FALSE is not returned",
                        transactionsService.saveTransactions(buildTransaction(11.2D, System.currentTimeMillis() - 100000)));
    }

    private Transaction buildTransaction(double amount, long timeStamp) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setTimeStamp(timeStamp);
        return transaction;
    }
}