package com.taskList.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para los datos de entrada del usuario.
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequestDto {
    private String username;
    private String password;
}
