package com.moksem.moksembank.model.entity;

public enum Role {
    ADMIN, USER;

    public String toString(){
        return name().toLowerCase();
    }
}
