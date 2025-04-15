package com.taskList.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Creacion de la tabla SQL y representación de esta en la aplicación.
@Data //Generar automaticamente getters y setters del objeto
@AllArgsConstructor //Constructor con todos los argumentos
@NoArgsConstructor //Constructor sin argumentos.
@Entity //Definir que es una entidad
@Table(name = "task")
public class TaskEntity {

    @Id //Definir que esta es una clave primaria.
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Definir que el valor del id sera generado automaticamente por la BBDD.
    private int id;
    private String petition;
    private String status;
}
