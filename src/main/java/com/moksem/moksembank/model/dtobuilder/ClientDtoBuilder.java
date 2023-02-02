package com.moksem.moksembank.model.dtobuilder;

import com.moksem.moksembank.model.dto.ClientDto;
import com.moksem.moksembank.model.entity.User;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientDtoBuilder {
    public static ClientDto getClientDto(User user) {
        return ClientDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .middleName(user.getMiddleName())
                .phoneNumber(user.getPhoneNumber())
                .password(user.getPassword())
                .build();
    }

    public static List<ClientDto> getClientsDto(List<User> users) {
        List<ClientDto> usersDto = new ArrayList<>();
        if (users.isEmpty())
            return usersDto;

        users.forEach(user -> usersDto.add(getClientDto(user)));
        return usersDto;
    }
}
