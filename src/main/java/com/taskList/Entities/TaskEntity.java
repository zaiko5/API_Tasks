package com.taskList.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Creacion de la tabla SQL.
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class TaskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String petition;
    private String status;
}
