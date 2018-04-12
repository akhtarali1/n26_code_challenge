/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.controller;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import com.n26.challenge.backendrest.service.StatisticsServiceImpl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.DoubleSummaryStatistics;

/**
 * @author Akhtar on 02-Apr-18.
 */
@RestController
@RequestMapping(value = "/statistics", consumes = APPLICATION_JSON_VALUE)
public class StatisticsController {

    private final StatisticsServiceImpl statisticsService;

    /**
     * @param statisticsService {@link StatisticsServiceImpl}
     */
    public StatisticsController(StatisticsServiceImpl statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * @return Statistics for transactions
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public DoubleSummaryStatistics getStatistics() {
        return statisticsService.getStatisticsForTime();
    }
}