package com.taskList.Controllers;

import com.taskList.DTOs.TaskDto;
import com.taskList.Services.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

//TEST DE CONTROLADORES.
@ExtendWith(MockitoExtension.class) //Permite inyeccion de dependencias en testeo.
class TaskControllersTest {

    @InjectMocks //Inyectamos el mock de la clase que vamos a testear.
    private TaskControllers controller;

    @Mock //Inyectamos la dependencia de la clase a testear, en este caso los controllers dependen de los servicios.
    private TaskService taskService;

    /**
     * Nota 1: En este caso, los testeos solo tienen una ruta a seguir, ya que si se llega a la capa de controladores quiere decir que la respuesta es correcta, en caso de excepciones está la capa de ExceptionControllerAdvice.
     */

    /**
     * Nota 2: Los when se usan con los Mock que es la dependencia de la clase a testear, y los act se usan con los InjectMock que es directamente un objeto de la clase a testear.
     */

    /**
     * UNICO CASO, Test para el metodo getTasks de los controllers: Hay respuesta HTTP positiva.
     */
    @Test
    void getTasks() {
        // ARRANGE
        //Ocupamos una lista de objetos TaskDto que es lo que deberia de devolver el metodo getTask del servicio.
        List<TaskDto> mockTasks = List.of(
                new TaskDto(1, "Tarea 1", "Done"),
                new TaskDto(2, "Tarea 2", "To do")
        );
        //Cuando se llame al metodo de servicio getTask deberia retornar la lista de tareas.
        when(taskService.getTasks()).thenReturn(mockTasks);

        // ACT
        //Obteniendo un resultado llamando al metodo de la clase testeada.
        ResponseEntity<Object> response = controller.getTasks();

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode()); //La respuesta HTTP de el resultado debe ser un código 200.
    }

    @Test
    void getTaskID() {
        //ARRANGE
        TaskDto task = new TaskDto(1,"Hacer algo","Hecha");
        when(taskService.getTaskID(1)).thenReturn(task);

        //ACT
        ResponseEntity<Object> response = controller.getTaskID(task.getId());

        //ASSERT
        assertEquals((HttpStatus.OK), response.getStatusCode());
        assertEquals(task, response.getBody());
    }

    @Test
    void postTask() {
        //ARRANGE
        TaskDto task = new TaskDto(0,"Hacer algo","Hecha");
        TaskDto taskReturned = new TaskDto(1,"Hacer algo","Hecha");
        when(taskService.postTask(task)).thenReturn(taskReturned);

        //ACT
        ResponseEntity<Object> response = controller.postTask(task);

        //ASSERT
        assertEquals((HttpStatus.CREATED), response.getStatusCode());
        assertEquals(taskReturned, response.getBody());
    }

    @Test
    void putTaskID() {
        //ARRANGE
        int id = 1;
        TaskDto task = new TaskDto(0,"Hacer algo","Hecha");
        TaskDto taskReturned = new TaskDto(id,"Hacer algo","Hecha");
        when(taskService.putTask(task, id)).thenReturn(taskReturned);

        //ACT
        ResponseEntity<Object> response = controller.putTaskID(id, task);

        //ASSERT
        assertEquals((HttpStatus.OK), response.getStatusCode());
        assertEquals(taskReturned, response.getBody());
    }

    @Test
    void patchTaskID() {
        //ARRANGE
        int id = 1;
        TaskDto task = new TaskDto(0,"Hacer otra cosa",null);
        TaskDto taskReturned = new TaskDto(id,task.getPetition(),"Hecha");
        when(taskService.patchTask(task, id)).thenReturn(taskReturned);

        //ACT
        ResponseEntity<Object> response = controller.patchTaskID(id, task);

        //ASSERT
        assertEquals((HttpStatus.OK), response.getStatusCode());
        assertEquals(taskReturned, response.getBody());
    }

    @Test
    void deleteTaskID() {
        //ARRANGE
        int id = 1; //Solo necesitamos un id existente.
        doNothing().when(taskService).deleteTask(id); //Cuando se ejecute el metodo deleteTask de taskService no se hace nada.

        //ACT
        ResponseEntity<Object> response = controller.deleteTaskID(id); //Obteniendo el resultado de la ejecucion del metodo (codigo de respuesta)

        //ASSERT
        assertEquals((HttpStatus.NO_CONTENT), response.getStatusCode()); //Comparando que el estado de la respuesta sea no content.
    }
}