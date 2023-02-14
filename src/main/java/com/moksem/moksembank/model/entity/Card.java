package com.moksem.moksembank.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * Card entity class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Card extends Entity{
    private User user;
    private BigDecimal wallet;
    private String number;
    private boolean status;
}

