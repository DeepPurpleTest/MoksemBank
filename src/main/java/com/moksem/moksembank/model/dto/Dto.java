package com.moksem.moksembank.model.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

@Data
public abstract class Dto {
    Set<Param> errors = new HashSet<>();
    public record Param(@NonNull String errorName, @NonNull String message) {
    }
}
