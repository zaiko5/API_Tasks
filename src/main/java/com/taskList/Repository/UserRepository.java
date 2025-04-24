package com.taskList.Repository;

import com.taskList.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

//Clase para poder hacer consultas a la BD regresando objetos del tipo Usuario, verificar si está registrado o no.
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username); //Encontrar usuarios por el nombre de usuario.
    boolean existsByUsername(String username); //Verificar si un usuario existe por su username
}
