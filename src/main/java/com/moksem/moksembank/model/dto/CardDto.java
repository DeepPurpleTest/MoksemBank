package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Card entity dto class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class CardDto extends Dto{
    private String id;
    private String number;
    private String wallet;
    private boolean status;
    private boolean request;
}
