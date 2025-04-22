package com.taskList.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Clase que va a definir el comportamiento de un objeto usuario en la BD, no necesita relacion con las tareas, ya que aqui solo se guardará informacion para las credenciales del usuario.
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String rol; //"USER" o "ADMIN"

    //Constructor sin id para cambiar de DTOSignin a UserEntity para guardarlo en la BDD.
    public UserEntity(String username, String password, String rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }
}
