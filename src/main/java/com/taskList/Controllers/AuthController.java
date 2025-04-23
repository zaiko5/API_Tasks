package com.taskList.Controllers;

import com.taskList.Auth.JWTService;
import com.taskList.DTOs.UserRequestDto;
import com.taskList.ServiceImpl.UserServiceImpl;
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

    @Autowired
    private UserServiceImpl userService;

    /**
     * Peticion post con ruta login para iniciar sesion.
     * @param loginRequest pasado por JSON que tendrá el usuario y la contraseña
     * @return el token si es que el usuario y la psswd son correctos, si no, lanza una excepcion 403 (no autorizado)
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto loginRequest) { //Pidiendo datos desde el Front.
        String jwt = userService.login(loginRequest);
        return ResponseEntity.ok().body(Map.of("token", jwt)); //Retornar el token
    }

    /**
     * Peticion post para registrar a un usuario.
     * @param signinRequest Pasado por JSON con usuario y contraseña nuevos
     * @return Un codigo 200 diciendo que el usuario fue registrado con exito, si no, las excepciones se manejan en el servicio.
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody UserRequestDto signinRequest) {
        UserRequestDto user = userService.postUser(signinRequest); //Registrando al usuario en la BDD con el servicio.
        return ResponseEntity.ok().body("Usuario registrado con exito");
    }
}
