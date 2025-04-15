package com.taskList.Exception;

//Clase para lanzar la excepcion si la lista de notas no tiene ninguna nota.
public class ListaVaciaException extends RuntimeException{
    public ListaVaciaException(){
        super("La lista de notas no contiene ninguna nota");
    }
}
