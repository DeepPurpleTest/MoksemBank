package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private String id;
    private String name;
    private String surname;
    private boolean status;
    private boolean request;
}
