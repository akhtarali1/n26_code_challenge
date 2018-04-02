/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.domain;

import lombok.Data;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Data
public class Error {

    private Integer code;
    private String description;

}
