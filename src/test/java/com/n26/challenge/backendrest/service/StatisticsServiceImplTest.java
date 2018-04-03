/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.n26.challenge.backendrest.domain.N26Exception;
import com.n26.challenge.backendrest.domain.Statistics;
import com.n26.challenge.backendrest.repository.TransactionsRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

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

    @Test
    public void getStatisticsForTime() {
        when(transactionsRepository.findStatisticsBetweenTime(anyLong(), anyLong())).thenReturn(new Statistics(10.5D, 11D, 12D, 13D, 14L));
        Statistics statistics = statisticsService.getStatisticsForTime();
        assertEquals("Expected sum is 10.5", 10.5, statistics.getSum().doubleValue(), 0);
        assertEquals("Expected avg is 10.5", 11, statistics.getAvg().doubleValue(), 0);
        assertEquals("Expected max is 10.5", 12, statistics.getMax().doubleValue(), 0);
        assertEquals("Expected min is 10.5", 13, statistics.getMin().doubleValue(), 0);
        assertEquals("Expected count is 10.5", 14, statistics.getCount().longValue(), 0);
    }

    @Test
    public void getStatisticsForTimeWithCountZero() {
        when(transactionsRepository.findStatisticsBetweenTime(anyLong(), anyLong())).thenReturn(new Statistics(null, null, null, null, 0L));
        try {
            statisticsService.getStatisticsForTime();
        } catch (N26Exception e) {
            assertEquals("Expected error description is not returned", "No transactions happened in last 60 seconds", e.getError().getDescription());
            assertEquals("Expected error code is not returned", new Integer(2000), e.getError().getCode());
        }
    }
}