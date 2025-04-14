package com.taskList.ServiceImpl;

import com.taskList.Domain.Task;
import com.taskList.Entities.TaskEntity;
import com.taskList.Repository.TaskRepository;
import com.taskList.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private List<TaskEntity> tasks = new ArrayList<>(Arrays.asList(
            new TaskEntity(1, "Do nothing", "Done"),
            new TaskEntity(2, "Do the homework", "Not done"),
            new TaskEntity(3, "Learning about apis", "Not done"),
            new TaskEntity(4, "Learning about queries in spring boot", "To do")
    ));

    @Autowired
    @Lazy
    TaskRepository taskRepository;

    @Override
    public List<TaskEntity> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskID(int id) {
        for(Task t : tasks){
            if(t.getId() == id){
                return t;
            }
        }
        return null;
    }

    @Override
    public Task postTask(Task task) {
        if(task.getPetition() != null && !task.getPetition().isEmpty() && //Doesn't get a field petition null or void
        task.getStatus() != null && !task.getStatus().isEmpty()){ //Doesn't get a field status null or void.
            //If all the validations are met, we add the task to the task-list.
            task.setId(tasks.size() + 1);
            tasks.add(task);
            return task;
        }
        return null;
    }

    @Override
    public Task putTask(Task task) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == task.getId()) {
                tasks.set(i, task);
                return task;
            }
        }
        return null;
    }

    @Override
    public Task patchTask(Task task) {
        for(Task t : tasks){
            if(t.getId() == task.getId()){
                if(task.getPetition() != null && !task.getPetition().isEmpty()){
                    t.setPetition(task.getPetition());
                }
                if(task.getStatus() != null && !task.getStatus().isEmpty()){
                    t.setStatus(task.getStatus());
                }
                return task;
            }
        }
        return null;
    }

    @Override
    public boolean deleteTask(int id) {
        boolean removed = tasks.removeIf(t -> t.getId() == id);
        return removed;  // Si la tarea fue eliminada, removed será true, si no, será false.
    }
}
