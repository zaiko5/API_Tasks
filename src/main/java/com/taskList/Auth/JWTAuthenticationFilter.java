package com.taskList.Auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Interceptar el request, y validar (o no) el JWT y a su vez cargar el user en la DB, cuando no existe el token o es invalido, solo lo ignora.
//Aqui ya se lanzan excepciones que tienen que ver con el inicio de sesion o validacion del token, los mensajes genericos de las excepciones no se usan, solo son para que no se marque error.
@Component //Creando el componente
public class JWTAuthenticationFilter extends OncePerRequestFilter { //Con esta herencia, solo se ejecutará una vez por request

    @Autowired
    private JWTService jwtService; //Inyectando este servicio para extraer y validar el JWT.

    @Autowired
    private UserDetailsServiceImpl userDetailsService; //Inyectandoo este servicio para transformar los datos de JWTS a UDI.

    //intercepta cada request, valida cosas como el JWT, y decide si continúa al controlador.
    @Override
    protected void doFilterInternal(HttpServletRequest request, //Parametros por defecto de este metodo.
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException { //Puede lanzar una excepcion.

        String uri = request.getRequestURI();

        //Definiendo que solo la ruta login y signin van a ser pyublicas, seguirán con la cadena de filtros.
        if (uri.equals("/login") || uri.equals("/signin")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization"); //Extraer el valor del header authorization de la solicitud HTTP.

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            request.setAttribute("customErrorMessage", "Falta el token o es incorrecto");
            throw new JwtException("Authentication Error"); //Mensaje generico
        }

        //Capturando la excepcion.
        try {
            //Extrayendo el token generando un substring despues de los primeros 7 caracteres (bearer y el espacio).
            String token = authHeader.substring(7);

            //Extrayendo el username de el token.
            String username = jwtService.extraerUsername(token);

            // Si no se pudo extraer el username del token
            if (username == null) {
                request.setAttribute("customErrorMessage","Token inválido: no contiene información de usuario");
                throw new JwtException("Authentication Error"); //Mensaje generico
            }

            // Cargar detalles del usuario
            UserDetails userDetails;
            try { //Capturando excepcion.
                userDetails = userDetailsService.loadUserByUsername(username);
            } catch (UsernameNotFoundException ex) {
                request.setAttribute("customErrorMessage","Usuario no encontrado: " + ex.getMessage());
                throw new AuthenticationException("Authentication Error") {}; //M;ensaje generico, no se usa.
            }

            // Validar el token
            if (!jwtService.validarToken(token, userDetails)) {
                request.setAttribute("customErrorMessage","Token inválido o manipulado");
                throw new JwtException("Authentication Error"); //Mensaje generico
            }

            // Si llegamos aquí, el token es válido, establecer la autenticación
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Continuar con la cadena de filtros
            filterChain.doFilter(request, response);

            //Lanzando posibles excepciones sobre el token (No se usan los mensajes, el mensaje se define en el customAuthenticationEntryPoint.
        } catch (ExpiredJwtException ex) {
            request.setAttribute("customErrorMessage","El token ha expirado");
            throw new JwtException("Authentication Error"); //Mensaje generico
        } catch (UnsupportedJwtException ex) {
            request.setAttribute("customErrorMessage","Token no soportado");
            throw new JwtException("Authentication Error"); //Mensaje generico
        } catch (MalformedJwtException ex) {
            request.setAttribute("customErrorMessage","Token mal formado");
            throw new JwtException("Authentication Error"); //Mensaje generico
        } catch (JwtException ex) {
            throw ex; // Re-lanzar para que sea manejada por el GlobalExceptionHandler
        } catch (Exception ex) {
            request.setAttribute("customErrorMessage","Error en la autenticacion" + ex.getMessage());
            throw new JwtException("Authentication Error"); //Mensaje generico, no se usa.
        }
    }
}
