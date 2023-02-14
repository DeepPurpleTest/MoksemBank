package com.moksem.moksembank.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * User entity class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class User extends Entity {
    private String name;
    private String surname;
    private String middleName;
    private String password;
    private String phoneNumber;
    private boolean status;
    private int role;

}
