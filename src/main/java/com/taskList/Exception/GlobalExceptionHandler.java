package com.taskList.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice //Definiendo que sera una clase contenedora de handlers para manejar excepciones.
public class GlobalExceptionHandler {

    //Definiendo que en caso de que salga una excepcion del tipo ListaVaciaClass, se capturará de la siguiente manera.
    @ExceptionHandler(ListaVaciaException.class)
    public ResponseEntity<Object> handleListaVaciaException(ListaVaciaException e){ //Metodo que se lanzará
        //Se lanzará un codigo de error 404 mapenado la palabra errorcon el mensaje que tiene la excepcion.
        return ResponseEntity.status(404).body(Map.of("error: ", e.getMessage()));
    }
}
