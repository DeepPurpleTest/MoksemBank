package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class CardDto extends Dto{
    private long id;
    private String number;
    private String wallet;
    private boolean status;
    private boolean request;
}
