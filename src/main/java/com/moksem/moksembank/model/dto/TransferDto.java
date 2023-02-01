package com.moksem.moksembank.model.dto;

import com.moksem.moksembank.model.dto.entitydto.CardDto;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class TransferDto {
    private CardDto sender;
    private CardDto receiver;
    private String amount;
    private List<CardDto> cards;
    private Set<Param> errors;

    public record Param(@NonNull String errorName, @NonNull String message) {
    }
}

