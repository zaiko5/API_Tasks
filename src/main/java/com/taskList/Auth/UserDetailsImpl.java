package com.taskList.Auth;
import com.taskList.Entities.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

//Clase que adapta tu modelo de usuario al formato que necesita Spring Security para autenticación, define los retornos de username, pssw encriptada, los permisos que puede tener tu usuario, si la cuenta ya expiró, etc.
//Esta clase por lo general siempre tiene estas funciones.
@AllArgsConstructor //Constructor
public class UserDetailsImpl implements UserDetails{

    //Instancia de la clase userEntity.
    private UserEntity user;

    //Permite retornar una lista de roles y permisos del usuario.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRol())); //Retorno de una lista de Roles y permisos del usuario.
    }

    //Retornar la contraseña del usuario encriptada.
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    //Retornar el username del usuario
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //Retornar si la cuenta ya expiró
    @Override
    public boolean isAccountNonExpired() { return true; }

    //Retornar si la cuenta NO está bloqueada.
    @Override
    public boolean isAccountNonLocked() { return true; }

    //Retornar si la credencial no ha expirado.
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    //Retornar si está habilitado o activo.
    @Override
    public boolean isEnabled() { return true; }
}
