package com.taskList.ServiceImpl;
import com.taskList.Auth.JWTService;
import com.taskList.DTOs.UserRequestDto;
import com.taskList.Entities.UserEntity;
import com.taskList.Mappers.UserMapper;
import com.taskList.Repository.UserRepository;
import com.taskList.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

//Servicio para la logica de la creacion del user.
@Service
public class UserServiceImpl implements UserService {

    //Para la llamada a la DDBB.
    @Autowired
    UserRepository userRepository;

    //Para encriptar la contraseña
    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager; //Para autenticar con el username y el password pasado.

    @Autowired
    private JWTService jwtService; //Para generar el token.


    @Override
    public String login(UserRequestDto login){
        Authentication auth = authManager.authenticate(  //Verificando la autenticacion del usuario.
                new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String jwt = jwtService.generarToken(userDetails); //Generando el token si es que los datos son correctos.
        return jwt;
    }

    //Funcion para agregar un usuario a la base de datos.
    @Override
    public UserRequestDto postUser(UserRequestDto signinRequest) {
        //Verificando que ni el usuario ni la contraseña sean null
        if (signinRequest.getUsername() != null && !signinRequest.getUsername().isEmpty() &&
                signinRequest.getPassword() != null && !signinRequest.getPassword().isEmpty()) {

            //Verificando si el usuario existe en la BDD.
            boolean existsUsername = userRepository.existsByUsername(signinRequest.getUsername());

            //Si existe se lanza esta excepcion (A CAPTURAR)
            if (existsUsername) {
                throw new RuntimeException("El usuario ya existe"); // Excepción clara
            } else { //Si no, se guarda en la base de datos
                UserEntity userEntity = UserMapper.requestToDTO(signinRequest); //Lo mapeamos a entidad
                userEntity.setPassword(encoder.encode(userEntity.getPassword())); //Encriptamos la psswd
                userRepository.save(userEntity); // Guardar usando save
                return signinRequest; //Retornamos el usuario (no se usara igual)
            }
        } else { //Si el usuario o la contraseña estan mal, lanzamos excepcion (A CAPTURAR)
            throw new IllegalArgumentException("Username o password son inválidos");
        }
    }
}
