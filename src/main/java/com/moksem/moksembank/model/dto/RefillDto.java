package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Refill page dto class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class RefillDto extends Dto{
    private long id;
    private String number;
    private String amount;
    private String wallet;
    private boolean status;
}
