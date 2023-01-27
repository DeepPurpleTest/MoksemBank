package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CardDto {
    private long id;
    private String number;
    private String wallet;
    private boolean status;
    private boolean request;
}
