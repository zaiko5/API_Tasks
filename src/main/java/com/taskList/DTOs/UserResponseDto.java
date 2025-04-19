package com.taskList.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//DTO para datos de salida al usuario sobre su token.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private String token;
    private String tipo = "Bearer";
    private String username;
    private String rol;
}
