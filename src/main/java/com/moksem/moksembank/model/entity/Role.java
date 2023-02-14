package com.moksem.moksembank.model.entity;

/**
 * Role entity enum
 */
public enum Role {
    ADMIN, USER;

    @Override
    public String toString(){
        return name().toLowerCase();
    }
}
