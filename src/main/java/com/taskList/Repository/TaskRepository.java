package com.taskList.Repository;

import com.taskList.Entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repositorio que extiende de JpaRepository usado para llamar las funciones del JPA
//No se testeará, ya que no tiene metodos complejos, pero de hacerse el testeo deberia tener la anotacion @DataJpaTest
@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    TaskEntity findFirstByOrderByIdDesc(); //Funcion que encuentra el ultimo registro registrado.

}
