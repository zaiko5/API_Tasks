package com.taskList.Services;

import com.taskList.DTOs.TaskDto;

import java.util.List;

//Interfaz que para definir los servicios de la tarea.
public interface TaskService {

    List<TaskDto> getTasks(); //Return all the tasks (Get all the tasks petition)
    TaskDto getTaskID(int id); //Return just one task by ID.
    TaskDto postTask(TaskDto task); //Return just the task created.
    TaskDto putTask(TaskDto task, int id); //Return just the task modified.
    TaskDto patchTask(TaskDto task, int id); //Return just the task modified.
    void deleteTask(int id); //It doesn't return something.
}
