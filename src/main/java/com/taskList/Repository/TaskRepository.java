package com.taskList.Repository;

import com.taskList.Entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repositorio que extiende de JpaRepository usado para llamar las funciones del JPA
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findFirstByOrderByIdDesc(); //Funcion que encuentra el ultimo registro registrado.

}
