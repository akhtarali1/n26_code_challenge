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
import com.n26.challenge.backendrest.domain.Statistics;
import com.n26.challenge.backendrest.service.StatisticsServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
        given(statisticsService.getStatisticsForTime()).willReturn(new Statistics(10.5D, 10.6D, 10.4D, 10.3D, 10L));
        mockMvc.perform(get(URL)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.sum").value(10.5))
                        .andExpect(jsonPath("$.avg").value(10.6))
                        .andExpect(jsonPath("$.max").value(10.4))
                        .andExpect(jsonPath("$.min").value(10.3))
                        .andExpect(jsonPath("$.count").value(10));
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