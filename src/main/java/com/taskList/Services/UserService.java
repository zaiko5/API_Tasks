package com.taskList.Services;

import com.taskList.DTOs.UserRequestDto;

//Interfaz para definir los metodos del servicio del usuario.
public interface UserService {
    String login(UserRequestDto login);
    UserRequestDto postUser(UserRequestDto signinRequest);
}
