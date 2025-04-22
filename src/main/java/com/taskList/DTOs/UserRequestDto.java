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
    private final String rol = "USER"; //Rol estatico para cuando se quiera registrar un usuario no ueda modificarlo

    //Constructor con solo 2 argumentos para que no se pueda modificar el rol desde el constructor.
    public UserRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
