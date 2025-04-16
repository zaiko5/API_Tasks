package com.taskList.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Clase de los objetos que se mostrarán al cliente, solo se puede mostrar esta clase.
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    //Debe tener los mismos atributos o menos que la clase entidad.
    //No se usan las validaciones de valid, ya que ya se validan en excepciones personalizadas, pero es buena idea validarlas con @Valid.
    private int id;
    private String petition;
    private String status;
}
