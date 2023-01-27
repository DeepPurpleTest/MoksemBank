package com.moksem.moksembank.model.dtobuilder;

import com.moksem.moksembank.appcontext.AppContext;
import com.moksem.moksembank.model.dto.UserDto;
import com.moksem.moksembank.model.entity.User;
import com.moksem.moksembank.model.service.RequestService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserDtoBuilder {
    static RequestService requestService = AppContext.getInstance().getRequestService();
    public static UserDto getUserDto(User user){
        UserDto userDto = UserDto.builder()
                .id(String.valueOf(user.getId()))
                .name(user.getName())
                .surname(user.getSurname())
                .status(user.isStatus())
                .build();
        if (!requestService.findAllByUser(user).isEmpty())
            userDto.setRequest(true);

        return userDto;
    }

    public static List<UserDto> getUsersDto(List<User> users){
        List<UserDto> usersDto = new ArrayList<>();
        if (users.isEmpty())
            return usersDto;

        users.forEach(user -> usersDto.add(getUserDto(user)));
        return usersDto;
    }

//    public static List<UserDto> getUsersDto(User user){
//        List<UserDto> usersDto = new ArrayList<>();
//        if (user == null)
//            return usersDto;
//        usersDto.add(getUserDto(user));
//        return usersDto;
//    }
}
