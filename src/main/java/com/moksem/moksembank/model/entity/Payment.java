package com.moksem.moksembank.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment entity class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Payment extends Entity {
    private Card cardSender;
    private Card cardReceiver;
    private BigDecimal amount;
    private String status;
    private LocalDateTime date;
}

