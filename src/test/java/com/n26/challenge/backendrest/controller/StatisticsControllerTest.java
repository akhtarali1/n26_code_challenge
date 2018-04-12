/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.n26.challenge.backendrest.domain.N26Exception;
import com.n26.challenge.backendrest.service.StatisticsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

/**
 * @author Akhtar on 02-Apr-18.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(StatisticsController.class)
public class StatisticsControllerTest {

    private static final String URL = "/statistics";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private StatisticsServiceImpl statisticsService;

    /**
     * Get statistics from controller
     *
     * @throws Exception
     */
    @Test
    public void getStatistics() throws Exception {
        List<Double> records = Arrays.asList(10.3, 10.3, 10.4, 10.5);
        given(statisticsService.getStatisticsForTime()).willReturn(records.stream()
                                                                    .mapToDouble((x) -> x)
                                                                    .summaryStatistics());
        mockMvc.perform(get(URL)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sum").value(41.5))
                        .andExpect(jsonPath("$.average").value(10.375))
                        .andExpect(jsonPath("$.max").value(10.5))
                        .andExpect(jsonPath("$.min").value(10.3))
                        .andExpect(jsonPath("$.count").value(4));
    }

    /**
     * Error returned by translating N26Exception
     *
     * @throws Exception
     */
    @Test
    public void getStatisticsWithException() throws Exception {
        given(statisticsService.getStatisticsForTime()).willThrow(new N26Exception("No transaction present", 2000));
        mockMvc.perform(get(URL)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.code").value(2000))
                        .andExpect(jsonPath("$.description").value("No transaction present"));
    }
}