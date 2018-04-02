/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.domain;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Data
@NotNull
public class Transaction implements Serializable{

    private static final long serialVersionUID = 1L;

    @DecimalMin("0.01")
    private double amount;
    @Min(value = 0)
    private long timeStamp;
}