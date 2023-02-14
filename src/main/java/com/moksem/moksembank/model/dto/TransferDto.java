package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Transfer page dto class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class TransferDto extends Dto{
    private CardDto sender;
    private CardDto receiver;
    private String amount;
    private List<CardDto> cards;
}

