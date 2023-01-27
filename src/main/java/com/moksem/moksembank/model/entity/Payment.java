package com.moksem.moksembank.model.entity;

import lombok.*;

import java.math.BigDecimal;
import java.util.Calendar;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Payment extends Entity {
    private Card cardSender;
    private Card cardReceiver;
    private BigDecimal amount;
    private String status;
    private Calendar date;
}

