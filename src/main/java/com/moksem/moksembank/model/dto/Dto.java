package com.moksem.moksembank.model.dto;

import com.moksem.moksembank.util.DecoratorSet;
import lombok.Data;
import lombok.NonNull;

import java.util.HashSet;
import java.util.Set;

/**
 * Abstract dto class
 */
@Data
public abstract class Dto {
    private Set<Param> set = new HashSet<>();
    private DecoratorSet errors = new DecoratorSet(set);
    public record Param(@NonNull String errorName, @NonNull String message) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Param param = (Param) o;

            return errorName.equals(param.errorName);
        }

        @Override
        public int hashCode() {
            return errorName.hashCode();
        }
    }
}
