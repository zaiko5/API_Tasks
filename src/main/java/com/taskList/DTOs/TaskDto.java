package com.taskList.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Clase de los objetos que se mostrarán al cliente, solo se puede mostrar esta clase.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private int id;
    private String petition;
    private String status;
}
