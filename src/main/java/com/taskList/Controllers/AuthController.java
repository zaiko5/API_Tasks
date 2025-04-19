package com.taskList.Controllers;

import com.taskList.Auth.JWTService;
import com.taskList.DTOs.UserRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//Controlador con un solo endPoint para recibir datos y retornar el token al usuario.
@RestController
public class AuthController {
    @Autowired
    private AuthenticationManager authManager; //Para autenticar con el username y el password pasado.

    @Autowired
    private JWTService jwtService; //Para generar el token.

    //Peticion post para verificar si el usuario está o no registrado
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto loginRequest) { //Pidiendo datos desde el Front.
        Authentication auth = authManager.authenticate(  //Verificando la autenticacion del token.
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtService.generarToken(userDetails); //Generando el token si es que los datos son correctos.
        return ResponseEntity.ok().body(Map.of("token", jwt)); //Retornar el token
    }
}
