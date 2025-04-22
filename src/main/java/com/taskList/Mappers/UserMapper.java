package com.taskList.Mappers;

import com.taskList.DTOs.UserRequestDto;
import com.taskList.Entities.UserEntity;

//Mappeador de los USERS
public class UserMapper {

    //De userDTO a userEntity
    public static UserEntity requestToDTO(UserRequestDto userDto) {
        return new UserEntity(userDto.getUsername(), userDto.getPassword(), userDto.getRol());
    }
}
