package com.taskList.Controllers;

import com.taskList.DTOs.TaskDto;
import com.taskList.Domain.Task;
import com.taskList.Entities.TaskEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Lazy;
import com.taskList.Services.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskControllers {

    @Autowired
    @Lazy
    private TaskService taskService;

    @GetMapping
    public ResponseEntity<Object> getTasks() {
        List<TaskDto> tasks = taskService.getTasks();
        if (tasks.isEmpty()) {
            return ResponseEntity.status(404).body("No hay tareas para mostrar");
        }
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskID(@PathVariable int id) {
        Task task = taskService.getTaskID(id);
        if(task == null){
            return ResponseEntity.status(404).body("No se ha encontrado la tarea con id: " + id);
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Object> postTask(@RequestBody Task task){
        Task newTask = taskService.postTask(task);
        if(newTask == null){
            return ResponseEntity.status(400).body("Campos faltantes para el objeto: " + task);
        }
        return ResponseEntity.status(201).body(task);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> putTaskID(@PathVariable int id, @RequestBody Task task) {
        task.setId(id);
        Task newTask = taskService.putTask(task);

        if(newTask == null){
            return ResponseEntity.status(404).body("No se ha encontrado una tarea con id: " + id);
        }
        return ResponseEntity.ok(task);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchTaskID(@PathVariable int id, @RequestBody Task task) {
        task.setId(id);
        Task newTask = taskService.patchTask(task);
        if(newTask == null){
            return ResponseEntity.status(404).body("No se ha encontrado una tarea con id: " + id);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaskID(@PathVariable int id){
        boolean deleted = taskService.deleteTask(id);
        if(deleted){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).body("No se ha encontrado una tarea con id: " + id);
    }
}
