package com.moksem.moksembank.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class Entity implements Serializable {
    private long id;
}
