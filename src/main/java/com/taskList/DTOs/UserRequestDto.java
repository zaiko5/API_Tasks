package com.taskList.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para los datos de entrada del usuario.
@NoArgsConstructor
@Data
public class UserRequestDto {
    private String username;
    private String password;
    private final String rol = "USER";

    public UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
