/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.n26.challenge.backendrest.domain.N26Exception;
import com.n26.challenge.backendrest.entity.TransactionEntity;
import com.n26.challenge.backendrest.repository.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * @author Akhtar on 03-Apr-18.
 */
@RunWith(MockitoJUnitRunner.class)
public class StatisticsServiceImplTest {

    @InjectMocks
    private StatisticsServiceImpl statisticsService;

    @Mock
    private TransactionsRepository transactionsRepository;

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(statisticsService, "retrievalTimeFrameInSeconds", "60");
    }

    /**
     * Get statistics for a time frame(for last 60 seconds)
     */
    @Test
    public void getStatisticsForTime() {
        List<TransactionEntity> entityList = new ArrayList<>();
        entityList.add(formTransactionEntity(10.5, 156456L, 1L));
        entityList.add(formTransactionEntity(10.5, 156456L, 1L));
        entityList.add(formTransactionEntity(10.5, 156456L, 1L));
        entityList.add(formTransactionEntity(10.5, 156456L, 1L));
        entityList.add(formTransactionEntity(10.5, 156456L, 1L));

        when(transactionsRepository.findAllByTransactionTimeBetween(anyLong(), anyLong())).thenReturn(entityList);
        DoubleSummaryStatistics statistics = statisticsService.getStatisticsForTime();
        assertEquals("Expected sum is 10.5", 52.5, statistics.getSum(), 0);
        assertEquals("Expected avg is 10.5", 10.5, statistics.getAverage(), 0);
        assertEquals("Expected max is 10.5", 10.5, statistics.getMax(), 0);
        assertEquals("Expected min is 10.5", 10.5, statistics.getMin(), 0);
        assertEquals("Expected count is 10.5", 5, statistics.getCount(), 0);
    }

    /**
     * Get statistics with when no transactions happened in last 60 seconds
     */
    @Test
    public void getStatisticsForTimeWithCountZero() {
        when(transactionsRepository.findAllByTransactionTimeBetween(anyLong(), anyLong())).thenReturn(new ArrayList<>());
        try {
            statisticsService.getStatisticsForTime();
        } catch (N26Exception e) {
            assertEquals("Expected error description is not returned", "No transactions happened in last 60 seconds", e.getError().getDescription());
            assertEquals("Expected error code is not returned", new Integer(2000), e.getError().getCode());
        }
    }

    private TransactionEntity formTransactionEntity(double amount, long time, long id) {
        TransactionEntity entity = new TransactionEntity();
        entity.setAmount(amount);
        entity.setTransactionTime(time);
        entity.setId(id);
        return entity;
    }
}