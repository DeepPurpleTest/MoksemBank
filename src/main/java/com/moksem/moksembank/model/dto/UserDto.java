package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class UserDto extends Dto{
    private String name;
    private String surname;
    private String middleName;
    private String phoneNumber;

    private String id;
    private boolean status;
    private boolean request;
}
