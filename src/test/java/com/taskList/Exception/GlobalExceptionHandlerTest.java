package com.taskList.Exception;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Testeos para el handler y sus metodos (solo lanzan excepciones)
 */
@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    //Clase a testear.
    @InjectMocks
    GlobalExceptionHandler globalExceptionHandler;

    /**
     * UNICO CASO: Lanza una excepcion del tipo lista vacia.
     */
    @Test
    void handleListaVaciaException() {
        //ARRANGE
        ListaVaciaException exception = new ListaVaciaException(); //Una excepcion del tipo lista vacia pasada por parametro.

        //ACT
        ResponseEntity<?> response = globalExceptionHandler.handleListaVaciaException(exception); //Obtenemos la respuesta de el metodo.

        //ASSERTS
        assertNotNull(response); //La respuesta no debe ser nula
        assertEquals(404, response.getStatusCodeValue()); //El codigo de error es el 404
        Map<String, String> responseBody = (Map<String, String>) response.getBody(); //Obteniendo el body de la respuesta.
        assertNotNull(responseBody); //El body no debe ser null
        assertEquals(exception.getMessage(), responseBody.get("error: ")); //El mensaje de la excepcion debe ser igual al mapeo de el mensaje en el body de la respuesta.
    }

    /**
     * Unico caso> Lanza una excepcion del tipo tarea no encontrada.
     */
    @Test
    void handleTareaNoEncontradaException() {
        TareaNoEncontradaException exception = new TareaNoEncontradaException(1);

        ResponseEntity<?> response = globalExceptionHandler.handleTareaNoEncontradaException(exception);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody(); //Obteniendo el body de la respuesta.
        assertNotNull(responseBody);
        assertEquals(exception.getMessage(), responseBody.get("error: "));
    }

    /**
     * Unico caso> Lanza una excepcion del tipo Campos Faltantes.
     */
    @Test
    void handleCamposFaltantesException() {
        CamposFaltantesException exception = new CamposFaltantesException();

        ResponseEntity<?> response = globalExceptionHandler.handleCamposFaltantesException(exception);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
        Map<String, String> responseBody = (Map<String, String>) response.getBody(); //Obteniendo el body de la respuesta.
        assertNotNull(responseBody);
        assertEquals(exception.getMessage(), responseBody.get("error: "));
    }
}