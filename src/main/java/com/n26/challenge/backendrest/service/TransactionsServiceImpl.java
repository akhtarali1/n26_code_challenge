/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.service;

import com.n26.challenge.backendrest.domain.Transaction;
import com.n26.challenge.backendrest.entity.TransactionEntity;
import com.n26.challenge.backendrest.repository.TransactionsRepository;
import org.springframework.stereotype.Service;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Service
public class TransactionsServiceImpl {

    private static final int SIXTY_SECONDS = 60 * 1000;
    private final TransactionsRepository transactionsRepository;

    /**
     * @param transactionsRepository {@link TransactionsRepository}
     */
    public TransactionsServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    /**
     * Check transaction is valid and Save
     *
     * @param transaction to be saved in DB for getting statistics
     * @return FALSE if transaction happened before 60 seconds, TRUE if saved successfully
     */
    public boolean saveTransactions(Transaction transaction) {
        if ((System.currentTimeMillis() - transaction.getTimeStamp()) < SIXTY_SECONDS) {
            transactionsRepository.save(new TransactionEntity(transaction.getAmount(), transaction.getTimeStamp()));
            return true;
        } else {
            return false;
        }
    }

}