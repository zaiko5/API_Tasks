package com.taskList.Config;

import com.taskList.Entities.UserEntity;
import com.taskList.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

//Agregando un usuario a la DB.
@Component
public class InitUsuario implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername("Axel").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setUsername("Axel");
            user.setPassword(encoder.encode("1234"));
            user.setRol("USER");
            userRepository.save(user);
        }
    }
}
