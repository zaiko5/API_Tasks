package com.taskList.Exception;

//Excepcion para cuando haya campos faltantes.
public class CamposFaltantesException extends RuntimeException {
    public CamposFaltantesException() {
        super("Campos faltantes: petition o status");
    }
}
