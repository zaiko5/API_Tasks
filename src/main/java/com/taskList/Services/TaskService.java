package com.taskList.Services;

import com.taskList.DTOs.TaskDto;

import java.util.List;

public interface TaskService {

    List<TaskDto> getTasks(); //Return all the tasks (Get all the tasks petition)
    Object getTaskID(int id); //Return just one task by ID.
    Object postTask(TaskDto task); //Return just the task created.
    TaskDto putTask(TaskDto task, int id); //Return just the task modified.
    TaskDto patchTask(TaskDto task); //Return just the task modified.
    boolean deleteTask(int id); //It doesn't return something.
}
