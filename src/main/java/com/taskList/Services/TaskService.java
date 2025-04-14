package com.taskList.Services;

import com.taskList.DTOs.TaskDto;
import com.taskList.Domain.Task;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(); //Return all the tasks (Get all the tasks petition)
    Task getTaskID(int id); //Return just one task by ID.
    Task postTask(Task task); //Return just the task created.
    Task putTask(Task task); //Return just the task modified.
    Task patchTask(Task task); //Return just the task modified.
    boolean deleteTask(int id); //It doesn't return something.
}
