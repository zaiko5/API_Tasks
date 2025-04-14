package com.taskList.Controllers;

import com.taskList.DTOs.TaskDto;
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
        TaskDto task = (TaskDto) taskService.getTaskID(id);
        if(task == null){
            return ResponseEntity.status(404).body("No se ha encontrado la tarea con id: " + id);
        }
        return ResponseEntity.ok(task);
    }

    @PostMapping
    public ResponseEntity<Object> postTask(@RequestBody TaskDto task){
        TaskDto newTask = (TaskDto) taskService.postTask(task);
        if(newTask == null){
            return ResponseEntity.status(400).body("Campos faltantes para el objeto: " + task);
        }
        return ResponseEntity.status(201).body(newTask);
    }
}
