package com.taskList.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

//Clase para manejar las excepciones de errores de autenticacion.
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    //Funcion para manejar los errores de autenticacion.
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        //Le ponemos un status a la excepcion (401)
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Primero intenta obtener el mensaje personalizado con clave customErrorMessage
        String errorMessage = (String) request.getAttribute("customErrorMessage");

        // Si no hay mensaje personalizado, usa el de la excepción
        if (errorMessage == null) {
            errorMessage = authException.getMessage();
        }

        //Mostramos el mensaje de error
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        Map.of("error", errorMessage)
                )
        );
        //Todo esto se maneja gracias a que en el archivo de configuracion se manda a que las excepciones de tipo authenticationEntryPoint se manejen con el customAuthenticationEntryPoint (Esta clase)
    }
}
