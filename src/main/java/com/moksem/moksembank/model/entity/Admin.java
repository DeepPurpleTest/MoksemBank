package com.moksem.moksembank.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Admin entity class
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder(toBuilder = true)
public class Admin extends Entity{
    private String login;
    private String password;
    private int role;
}
