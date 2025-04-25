package com.taskList.ServiceImpl;

import com.taskList.Auth.JWTService;
import com.taskList.DTOs.UserRequestDto;
import com.taskList.Entities.UserEntity;
import com.taskList.Mappers.UserMapper;
import com.taskList.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //Permite inyeccion de dependencias en testeo.
class UserServiceImplTest {

    //Para la llamada a la DDBB.
    @Mock
    UserRepository userRepository;

    //Para encriptar la contraseña
    @Mock
    private PasswordEncoder encoder;

    @Mock
    private AuthenticationManager authManager; //Para autenticar con el username y el password pasado.

    @Mock
    private JWTService jwtService; //Para generar el token.

    @InjectMocks
    UserServiceImpl userServiceImpl;

    /**
     * Unico caso: El login sale con exito (no hay ninguna excepcion para tirar)
     */
    @Test
    void login() {
        // Arrange
        UserRequestDto loginRequest = new UserRequestDto("testuser", "testpassword"); //Usuario pasado por parametro
        Authentication authentication = mock(Authentication.class); // Mock de Authentication
        UserDetails userDetails = mock(UserDetails.class); // Mock de UserDetails
        String expectedJwt = "testJwtToken"; //Token esperado

        // Configurar el comportamiento de los mocks
        when(authManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication); //Cuando se llame a la funcion authenticate con un objeto como parametro UPAT debe retornar la autenticacion (un objeto del tipo authentication)
        when(authentication.getPrincipal()).thenReturn(userDetails); //Cuando se llame a la funcion getPrincipal se debe retornar un mock de la clase userDetails
        when(jwtService.generarToken(userDetails)).thenReturn(expectedJwt); //Cuando se llame a la funcion generarToken del servicio se retornara el token esperado.

        // Act
        String actualJwt = userServiceImpl.login(loginRequest); //Obteniendo la respuesta

        // Assert
        assertNotNull(actualJwt); //El token no debe de ser null
        assertEquals(expectedJwt, actualJwt); //El token obtenido debe ser igual al token esperado
    }

    /**
     * Caso 1: Sign in sin errores.
     */
    @Test
    void postUser() {
        // Arrange
        UserRequestDto signinRequest = new UserRequestDto("newuser", "securepassword"); //Usuario pasado por parametros
        String encodedPassword = "encodedSecurePassword"; //La contraseña "encriptada"
        UserEntity savedUserEntity = new UserEntity(); //Usuario guardado (objeto de la entidad usuario)
        savedUserEntity.setId(1L); //Id del usuario guardado
        savedUserEntity.setUsername(signinRequest.getUsername()); //Username del usuario guardado
        savedUserEntity.setPassword(encodedPassword); //Password del usuario guardado

        // Configurar el mock para el caso en que el usuario NO existe
        when(userRepository.existsByUsername(signinRequest.getUsername())).thenReturn(false); //Cuando se llame a la funcion del repositorio existsByUsername con parametro el username del usuario pasado por parametro se retornara false para hacer que no exista.
        when(encoder.encode(signinRequest.getPassword())).thenReturn(encodedPassword); //Cuando se llame a la funcion encode con la contraseña normal como parametro debe de retornar la contraseña encriptada.
        when(userRepository.save(any(UserEntity.class))).thenReturn(savedUserEntity); //Cuando se llame a la funcion save del repositorio se se debe retornar la entidad guardada.

        // Act
        UserRequestDto registeredUser = userServiceImpl.postUser(signinRequest); //Obteniendo los resultados

        // Assert
        assertNotNull(registeredUser); //El usuario registrado no debe ser null
        assertEquals(signinRequest.getUsername(), registeredUser.getUsername()); //El username del usuario pasado y del usuario registrado deben ser los mismos
        assertEquals(signinRequest.getPassword(), registeredUser.getPassword());//El passwd del usuario pasado y del usuario registrado deben ser los mismos
        // Verificar que el método save del repositorio fue llamado
        verify(userRepository).save(any(UserEntity.class));
    }

    /**
     * Caso 2.1: Hay username o password nulos.
     */
    @Test
    void postUser_NoSignInIfFieldsAreNullOrEmpty(){
        UserRequestDto signinRequest = new UserRequestDto(null, ""); //Usuario pasado por parametro con campos nulos y vacios

        //ACT + ASSERT
        assertThrows(IllegalArgumentException.class, () -> userServiceImpl.postUser(signinRequest)); //Como no se llamna a ningun metodo o no se hace nada antes de la verificacion, hacemos el assert + el act juntos verificando que lanza una excepcion
    }

    /**
     * Caso 2.2: El username si existe en la DB.
     */
    @Test
    void postUser_NoSignInIfUsernameAlreadyExists(){
        UserRequestDto signinRequest = new UserRequestDto("testuser", "testpassword"); //Usuario con username y password correctos

        when(userRepository.existsByUsername(signinRequest.getUsername())).thenReturn(true); //Pero cuando se verifica si el usuario existe es true, osea que ya existe

        //ACT + ASSERT
        assertThrows(RuntimeException.class, () -> userServiceImpl.postUser(signinRequest)); //Se verifica que se lance una excepcion del tipo runtimeExceptionClass al llamar al metodo con tales datos.
    }
}