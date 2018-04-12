/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.repository;

import com.n26.challenge.backendrest.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Akhtar on 02-Apr-18.
 */
public interface TransactionsRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * GET all transactions happened in period of time
     *
     * @param retrievalStartTime start time in milli seconds for getting statistics
     * @param retrievalEndTime   end time in milli seconds for getting statistics
     * @return all transactions in period of time
     */
    List<TransactionEntity> findAllByTransactionTimeBetween(long retrievalStartTime, long retrievalEndTime);

}