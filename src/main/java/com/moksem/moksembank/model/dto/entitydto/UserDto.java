package com.moksem.moksembank.model.dto.entitydto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class UserDto {
    private String name;
    private String surname;
    private String middleName;
    private String phoneNumber;

    private String id;
    private boolean status;
    private boolean request;
}
