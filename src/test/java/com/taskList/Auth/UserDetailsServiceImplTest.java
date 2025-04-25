package com.taskList.Auth;

import com.taskList.Entities.UserEntity;
import com.taskList.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private UserRepository userRepository;

    /**
     * Caso 1: Sin excepcion (testeo al UserDetailsServiceImpl y al UserDetailsImpl)
     */
    @Test
    void loadUserByUsername() {
        String username = "username123"; //Username pasado por parametro
        UserEntity userEntity = new UserEntity(username, "password", "USER"); //Usuario encontrado por el repositorio

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity)); //Cuando se llame a la funcion findByUsername con el username como parametro se debe de retornar un optional con el userEntity

        //ACT: Obteniendo el resultado del llamado a la funcion.
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(username);

        //Asserts
        assertEquals(username, userDetails.getUsername()); //El username pasado debe ser igual al username del usuario encontrado
        assertEquals("password", userDetails.getPassword()); //El password de la entidad encontrada debe ser igual al
        assertNotNull(userDetails); //El user retornado no debe ser null
        //Verificando los metodos del user retornado (asi se verifica tambien la clase UserDetailsImpl)
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isEnabled());
        assertEquals(1, userDetails.getAuthorities().size());
    }
}