package com.taskList.Auth;

import com.taskList.Entities.UserEntity;
import com.taskList.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//cargar los detalles de un usuario desde la base de datos y los adapta a Spring Security (Para eso se usa UserDetailsImpl)
@Service
public class UserDetailsServiceImpl implements UserDetailsService { //Implementacion de UserDetailsService

    //Inyeccion de dependencias.
    @Autowired
    private UserRepository userRepository;

    //Funcion que regresa un objeto UserDetails (En este caso uno hijo), este objeto tiene las funciones de getUsername, getPassword y todas las implementadas en UserDetailsImpl.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { //Puede lanzar una excepcion (falta manejarla desde el handler).
        UserEntity user = userRepository.findByUsername(username) //Crea el nuevo objeto si es que fue encontrado
                //Si no, retorna una excepcion.
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
        return new UserDetailsImpl(user); //Si no lanza la excepcion, retorna el user de forma UDI.z
    }
}
