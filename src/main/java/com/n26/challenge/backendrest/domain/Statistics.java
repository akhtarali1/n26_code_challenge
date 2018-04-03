/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.domain;

import lombok.Getter;

import java.io.Serializable;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Getter
public class Statistics implements Serializable {

    private static final long serialVersionUID = 1L;
    private Double sum;
    private Double avg;
    private Double max;
    private Double min;
    private Long count;

    /**
     * @param sum     of all transactions happened im time frame
     * @param average of all transactions happened im time frame
     * @param max     of all transactions happened im time frame
     * @param min     of all transactions happened im time frame
     * @param count   of all transactions happened im time frame
     */
    public Statistics(Double sum, Double average, Double max, Double min, Long count) {
        this.sum = sum;
        this.avg = average;
        this.max = max;
        this.min = min;
        this.count = count;
    }

}