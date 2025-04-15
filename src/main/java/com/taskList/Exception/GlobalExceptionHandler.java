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

    //Cuando salga la excepcion de este tipo, se llamará a esta funcion para capturar esa excepcion.
    @ExceptionHandler(TareaNoEncontradaException.class)
    //Usamos como parámetro el objeto de la excepcion, se pasará automaticamente cuando se lance la excepcion.
    public ResponseEntity<Object> handleTareaNoEncontradaException(TareaNoEncontradaException e){
        //Cuando se lance la excepcion se retornará un codigo de error 404 con el mensaje de la excepcion.
        return ResponseEntity.status(404).body(Map.of("error: ", e.getMessage()));
    }

    //Definiendo que este metodo solo se usará cuando haya una excepcion del tipo camposFaltantes.
    @ExceptionHandler(CamposFaltantesException.class)
    public ResponseEntity<Object> handleCamposFaltantesException(CamposFaltantesException e){
        //Retornamos un codigo de error 400 con el mensaje de la excepcion.
        return ResponseEntity.status(400).body((Map.of("error: ", e.getMessage())));
    }
}
