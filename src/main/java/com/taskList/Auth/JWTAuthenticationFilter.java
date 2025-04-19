package com.taskList.Auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Interceptar el request, y validar (o no) el JWT y a su vez cargar el user en la DB, cuando no existe el token o es invalido, solo lo ignora.
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

        String authHeader = request.getHeader("Authorization"); //Extraer el valor del header authorization de la solicitud HTTP.

        //SI el header no es null y empieza con bearer...
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            //Extrayendo el token generando un substring despues de los primeros 7 caracteres (bearer y el espacio).
            String token = authHeader.substring(7);
            //Extrayendo el username de el token.
            String username = jwtService.extraerUsername(token);

            //Si el username no es null y el contexto de seguridad de la app tiene una autenticacion valida.
            //SI getAuthentication es null, entonces no hay un usuario autenticado y se debe realizar el proceso de autenticación basado en el token
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //Creamos  un objeto userDetails en base a UDI y su metodo loadByUserName
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //Si la validacion del token es true...
                if (jwtService.validarToken(token, userDetails)) {
                    //hacer que el usuario esté autenticado en la sesión actual de la aplicación.
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); //Pasandole los roles y permisos del usuario.

                    // Se agregan detalles de la solicitud HTTP al token de autenticación
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        //continuar el procesamiento de la solicitud HTTP a través de la cadena de filtros.
        filterChain.doFilter(request, response);
    }
}
