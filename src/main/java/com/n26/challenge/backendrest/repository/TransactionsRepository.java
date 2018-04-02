/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.repository;

import com.n26.challenge.backendrest.domain.Statistics;
import com.n26.challenge.backendrest.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Akhtar on 02-Apr-18.
 */
public interface TransactionsRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * Get statistics of SUM, AVG, MIN, MAX & COUNT for all transactions happened in time frame
     *
     * @param retrievalStartTime start time in milli seconds for getting statistics
     * @param retrievalEndTime   end time in milli seconds for getting statistics
     * @return statistics for all transactions happened between time frame.
     */
    @Query("select new com.n26.challenge.backendrest.domain.Statistics" +
                    "(SUM(entity.amount), AVG(entity.amount), MAX(entity.amount), MIN(entity.amount), COUNT(entity)) from TransactionEntity entity " +
                    "where entity.transactionTime >= :retrievalTimeStart AND entity.transactionTime < :retrievalTimeEnd")
    Statistics findStatisticsBetweenTime(@Param("retrievalTimeStart") long retrievalStartTime, @Param("retrievalTimeEnd") long retrievalEndTime);

}