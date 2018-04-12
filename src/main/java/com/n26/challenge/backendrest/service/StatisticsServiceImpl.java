/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.service;

import com.n26.challenge.backendrest.domain.N26Exception;
import com.n26.challenge.backendrest.entity.TransactionEntity;
import com.n26.challenge.backendrest.repository.TransactionsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Service
public class StatisticsServiceImpl {

    private final TransactionsRepository transactionsRepository;
    @Value("${retrievalTimeFrameInSeconds}")
    private String retrievalTimeFrameInSeconds;

    /**
     * @param transactionsRepository {@link TransactionsRepository}
     */
    public StatisticsServiceImpl(TransactionsRepository transactionsRepository) {
        this.transactionsRepository = transactionsRepository;
    }

    /**
     * Get statistics of SUM, AVG, MIN, MAX & COUNT for all transactions happened in a time frame
     *
     * @return statistics of last 60 seconds transactions
     */
    public DoubleSummaryStatistics getStatisticsForTime() {
        long currentEpochTime = System.currentTimeMillis();
        // epoch time in milli seconds before 60 seconds of current time
        long statisticsStartTime = currentEpochTime - (Integer.valueOf(retrievalTimeFrameInSeconds) * 1000);
        List<TransactionEntity> recentTransactions = transactionsRepository.findAllByTransactionTimeBetween(statisticsStartTime, currentEpochTime);
        DoubleSummaryStatistics statistics = recentTransactions.stream()
                        .mapToDouble(TransactionEntity::getAmount)
                        .summaryStatistics();
        if (statistics.getCount() == 0) {
            throw new N26Exception("No transactions happened in last " + retrievalTimeFrameInSeconds + " seconds", 2000);
        } else {
            transactionsRepository.deleteAll(recentTransactions);
        }

        return statistics;
    }
}