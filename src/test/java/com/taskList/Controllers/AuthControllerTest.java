package com.taskList.Controllers;

import com.taskList.DTOs.UserRequestDto;
import com.taskList.ServiceImpl.UserServiceImpl;
import com.taskList.Services.UserService;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Nota 1: En este caso, los testeos solo tienen una ruta a seguir, ya que si se llega a la capa de controladores quiere decir que la respuesta es correcta, en caso de excepciones está la capa de ExceptionControllerAdvice.
 */

/**
 * Nota 2: Los when se usan con los Mock que es la dependencia de la clase a testear, y los act se usan con los InjectMock que es directamente un objeto de la clase a testear.
 */

@ExtendWith(MockitoExtension.class) //Permite inyeccion de dependencias en testeo (Obligatoria, si no inicializar dependencias con @BeforeEach.
class AuthControllerTest {

    @InjectMocks
    AuthController authController;

    @Mock
    UserService userService;

    /**
     * Unico caso: el login sale correctamente y regresa el token.
     */
    @Test
    void login() {
        // Arrange
        String expectedToken = "tokenDePrueba123"; //Token de prueba
        UserRequestDto loginRequest = new UserRequestDto("usuario", "contraseña"); //Usuario requestDto con datos de prueba.
        when(userService.login(loginRequest)).thenReturn(expectedToken); //Cuando se llame al servicio (dependencia) con el loginRequest se debe retornar el token.

        // Act
        ResponseEntity<?> response = authController.login(loginRequest); //Al llamar al metodo testeado (login del controlador), obtendremos una respuesta.

        // Assert
        assertNotNull(response); //Respuesta no nula.
        assertEquals(HttpStatus.OK, response.getStatusCode()); //El codigo de respuesta debe ser un 200.

        @SuppressWarnings("unchecked") //Suprimiendo advertencias "unchequeadas"
        Map<String, String> responseBody = (Map<String, String>) response.getBody(); //Obteniendo el body de la respuesta.
        assertNotNull(responseBody); //El body no deberia de ser null
        assertEquals(expectedToken, responseBody.get("token")); //El token inicial deberia de ser igual al mapeo del body de "token".

        // Verificar que el servicio fue llamado
        verify(userService).login(loginRequest);
    }

    /**
     * UNICO CASO: El usuario se registra correctamente.
     */
    @Test
    void signin() {
        //Arrange
        UserRequestDto user = new UserRequestDto("usernamenew", "password"); //Usuario con datos validos.

        when(userService.postUser(user)).thenReturn(user); //Cuando se llame al servicio con el metodo postUser, se va a retornar el mismo usuario.

        //Act
        ResponseEntity<?> response = authController.signin(user); //Obteniendo la respuesta del controlador en el metodo signin con nuestro usuario.

        // Assert
        assertNotNull(response); //La respuesta no debe ser nula
        assertEquals(HttpStatus.OK, response.getStatusCode()); //El codigo de estado debe ser 200
        String responseBody = (String) response.getBody(); //Obteniendo el body de la respuesta
        assertNotNull(responseBody); //El body no debe de ser null
        assertEquals("Usuario registrado con exito", responseBody); //El body debe coincidir con el texto esperado.
    }
}