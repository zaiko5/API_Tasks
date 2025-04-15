package com.taskList.Exception;

//Clase de la excepcion que se lanzará cuando no se encuentre la tarea.
public class TareaNoEncontradaException extends RuntimeException {

    public TareaNoEncontradaException(int id) {
        super("Tarea con id: " + id + " inexistente."); //Mensaje con el id de la tarea que causo la excepcion.
    }
}
