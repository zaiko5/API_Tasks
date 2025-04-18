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

@ExtendWith(MockitoExtension.class)
class TaskControllersTest {

    @InjectMocks
    private TaskControllers controller;

    @Mock
    private TaskService taskService;

    @Test
    void getTasks() {
        // ARRANGE
        List<TaskDto> mockTasks = List.of(
                new TaskDto(1, "Tarea 1", "Done"),
                new TaskDto(2, "Tarea 2", "To do")
        );
        when(taskService.getTasks()).thenReturn(mockTasks);

        // ACT
        ResponseEntity<Object> response = controller.getTasks();

        // ASSERT
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockTasks, response.getBody());
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
        int id = 1;
        doNothing().when(taskService).deleteTask(id);

        ResponseEntity<Object> response = controller.deleteTaskID(id);

        //ASSERT
        assertEquals((HttpStatus.NO_CONTENT), response.getStatusCode());
    }
}