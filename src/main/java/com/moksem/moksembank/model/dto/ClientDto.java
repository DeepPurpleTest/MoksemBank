package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Client entity dto class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class ClientDto extends Dto{
    private String name;
    private String surname;
    private String middleName;
    private String phoneNumber;
    private String password;

}
