package com.moksem.moksembank.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class AdminDto {
    private String login;
    private String password;
}
