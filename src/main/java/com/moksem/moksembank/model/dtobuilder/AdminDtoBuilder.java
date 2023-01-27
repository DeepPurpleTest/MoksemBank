package com.moksem.moksembank.model.dtobuilder;

import com.moksem.moksembank.model.dto.AdminDto;
import com.moksem.moksembank.model.entity.Admin;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminDtoBuilder {
    public static AdminDto getAdminDto(Admin admin){
        return AdminDto.builder()
                .login(admin.getLogin())
                .password(admin.getPassword())
                .build();
    }
}
