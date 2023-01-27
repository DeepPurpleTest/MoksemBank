package com.moksem.moksembank.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Request extends Entity{
    private String cardNumber;
    private long userId;
    private long adminId;
    private boolean status;
}

