package com.taskList.ServiceImpl;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;
import com.taskList.Exception.CamposFaltantesException;
import com.taskList.Exception.ListaVaciaException;
import com.taskList.Exception.TareaNoEncontradaException;
import com.taskList.Repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TaskServiceImplTest {

    @Mock //Simulando la base de datos.
    TaskRepository taskRepository;

    @InjectMocks //Inyectando el servicio a testear.
    TaskServiceImpl taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTasks() {
        //Datos de entrada
        List<TaskEntity> tareasMockeadas = List.of(
                new TaskEntity(1, "Tarea 1", "Done"),
                new TaskEntity(2, "Tarea 2", "To do")
        );
        when(taskRepository.findAll()).thenReturn(tareasMockeadas);

        List<TaskDto> resultado = taskService.getTasks();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Tarea 2", resultado.get(1).getPetition());
    }

    @Test
    void getTasks_throwException(){
        //Datos de entrada
        List<TaskEntity> tareasMockeadas = List.of(); //Lista vacía (no hay tareas)

        when(taskRepository.findAll()).thenReturn(tareasMockeadas);

        assertThrows(ListaVaciaException.class, () -> taskService.getTasks());
    }

    @Test
    void getTaskID() {
        //Datos de entrada.
        TaskEntity tareaMockeada = new TaskEntity(1, "Tarea 1", "To do");

        when(taskRepository.findById((long) tareaMockeada.getId())).thenReturn(Optional.of(tareaMockeada));

        TaskDto resultado = taskService.getTaskID(tareaMockeada.getId());

        assertNotNull(resultado);
        assertEquals("Tarea 1", resultado.getPetition());
    }

    @Test
    void getTaskID_throwException() {
        // Datos de entrada.
        int tareaId = 999; // ID de tarea que no existe.

        // Simulando que no se encuentra la tarea.
        when(taskRepository.findById((long) tareaId)).thenReturn(Optional.empty());

        // Verificando que se lance la excepción.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.getTaskID(tareaId));
    }

    @Test
    void postTask() {
        //Arrange
        TaskDto dtoEntrada = new TaskDto(0,"Estudiar Spring", "To do"); //Ejecucion metodo
        TaskEntity entidadEntrada = new TaskEntity(0, "Estudiar Spring", "To do"); //Para la comparacion
        TaskEntity entidadGuardada = new TaskEntity(1, "Estudiar Spring", "To do"); //Retorno en la comparacion
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Spring", "To do"); //Para la comparacion de asserts.

        //Definiendo que es lo que debe retornar el metodo con un valor x.
        when(taskRepository.save(entidadEntrada)).thenReturn(entidadGuardada);

        //Act
        TaskDto resultado = taskService.postTask(dtoEntrada); //Llamando a la funcion para obtener el resultado

        //Assert
        assertEquals(dtoEsperado.getId(), resultado.getId()); //Definiendo que el id del dtoEsperado debe ser igual al del resultado.
        assertEquals(dtoEsperado.getPetition(), resultado.getPetition());
        assertEquals(dtoEsperado.getStatus(), resultado.getStatus());
        assertNotNull(resultado);
    }

    @Test
    void postTask_throwException(){
        //Arrange
        TaskDto dtoEntrada = new TaskDto(0,"", "null");

        // Act + Assert
        assertThrows(CamposFaltantesException.class, () -> taskService.postTask(dtoEntrada));
    }

    @Test
    void putTask() {
    }

    @Test
    void patchTask() {
    }

    @Test
    void deleteTask() {
    }
}