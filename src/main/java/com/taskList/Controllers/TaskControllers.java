package com.taskList.Controllers;

import com.taskList.DTOs.TaskDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.context.annotation.Lazy;
import com.taskList.Services.TaskService;

import java.util.List;

//Clase controladora que interactua con el front.
@RestController
@RequestMapping("/tasks")
public class TaskControllers {

    //En el servicio se inyecta el repositorio para hacer queries desde ahi, en el controlador se inyecta el servicio para llamar a las funciones directamente desde el controlador.
    @Autowired //Inyectando dependencias (el servicio task para tener sus funciones)
    @Lazy
    private TaskService taskService;

    /**
     * Peticion get para obtener todas las tareas de la BBDD.
     * @return Un mensaje 200, la excepcion se maneja en el servicio con el handler.
     */
    @GetMapping
    public ResponseEntity<Object> getTasks() {
        List<TaskDto> tasks = taskService.getTasks(); //Obteniendo las listas de tareas desde el servicio.
        return ResponseEntity.ok(tasks); //Retornando el codigo de salida 200 y la lista de tareas.
    }

    /**
     * Peticion get para obtener una tarea por id
     * @param id pasado por path
     * @return un codigo 200, la excepcion se maneja en el servicio con el handler.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getTaskID(@PathVariable int id) {
        TaskDto task = taskService.getTaskID(id); //Obtenemos la tarea desde el servicio
        return ResponseEntity.ok(task); //Retornando el codigo de salida 200 y la tarea seleccionada.
    }

    /**
     * Peticion post para agregar una nueva tarea
     * @param task pasado por JSON
     * @return el codigo de estado 201, la excepcion se maneja en el servicio con el handler en cuanto se lanza.
     */
    @PostMapping
    public ResponseEntity<Object> postTask(@RequestBody TaskDto task){
        TaskDto newTask = taskService.postTask(task);
        return ResponseEntity.status(201).body(newTask);
    }

    /**
     * Peticion put para modificar los detalles de una tarea totalmente
     * @param id pasado por path
     * @param task pasado por JSON
     * @return Un mensaje de estado 200, la excepcion se maneja en el servicio con el handler en cuanto se lance.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Object> putTaskID(@PathVariable int id, @RequestBody TaskDto task) {
        TaskDto taskDto = taskService.putTask(task, id);
        return ResponseEntity.ok(taskDto);
    }

    /**
     * Peticion patch para modificar una tarea parcialmente
     * @param id pasado por path
     * @param task pasado por JSON
     * @return un codigo de estado
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Object> patchTaskID(@PathVariable int id, @RequestBody TaskDto task) {
        TaskDto newTask = taskService.patchTask(task, id);
        if(newTask == null){
            return ResponseEntity.status(404).body("No se ha encontrado una tarea con id: " + id);
        }
        return ResponseEntity.ok().body(newTask);
    }

    /**
     * Peticion delete para eliminar una tarea
     * @param id pasado por path
     * @return Un codigo de estado.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTaskID(@PathVariable int id){
        boolean deleted = taskService.deleteTask(id);
        if(deleted){
            return ResponseEntity.status(204).build();
        }
        return ResponseEntity.status(404).body("No se ha encontrado una tarea con id: " + id);
    }
}
