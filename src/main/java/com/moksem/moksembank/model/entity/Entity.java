package com.moksem.moksembank.model.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Abstract entity class
 */
@Data
public abstract class Entity implements Serializable {
    private long id;
}
