/*
 * Copyright (c) N26. All Rights Reserved.
 * ============================================================
 */
package com.n26.challenge.backendrest.entity;

import com.n26.challenge.backendrest.domain.Transaction;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author Akhtar on 02-Apr-18.
 */
@Data
@Entity
@Table(name = "N26_TRANSACTIONS")
@NoArgsConstructor
public class TransactionEntity {

    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "N26_TRANSACTION_SEQ", sequenceName = "N26_TRANSACTION_SEQ", allocationSize = 1)
    @GeneratedValue(generator = "N26_TRANSACTION_SEQ")
    private long id;

    @Column(name = "AMOUNT")
    private double amount;
    @Column(name = "TRANSACTION_TIME")
    private long transactionTime;

    /**
     * @param amount transaction amount
     * @param transactionTime transaction time
     */
    public TransactionEntity(double amount, long transactionTime) {
        this.amount = amount;
        this.transactionTime = transactionTime;
    }
}