package com.taskList.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        // Primero intenta obtener el mensaje personalizado
        String errorMessage = (String) request.getAttribute("customErrorMessage");

        // Si no hay mensaje personalizado, usa el de la excepción
        if (errorMessage == null) {
            errorMessage = authException.getMessage();
        }

        response.getWriter().write(
                new ObjectMapper().writeValueAsString(
                        Map.of("error", errorMessage)
                )
        );
    }
}
