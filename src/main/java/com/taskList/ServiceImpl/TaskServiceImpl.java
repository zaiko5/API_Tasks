package com.taskList.ServiceImpl;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;
import com.taskList.Mappers.TaskMapper;
import com.taskList.Repository.TaskRepository;
import com.taskList.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    public List<TaskDto> getTasks() {
        List<TaskEntity> entities = taskRepository.findAll();
        ArrayList<TaskDto> dtos = new ArrayList<>();
        for (TaskEntity entity : entities) {
            dtos.add(TaskMapper.toDTO(entity));
        }
        return dtos;
    }

    @Override
    public Object getTaskID(int id) {
        Optional<TaskEntity> taskOptional = taskRepository.findById((long) id);
        if (taskOptional.isPresent()) {
            TaskEntity task = taskOptional.get();
            TaskDto taskDto = TaskMapper.toDTO(task);
            return taskDto;
        } else {
            return null;
        }
    }

    @Override
    public Object postTask(TaskDto task) {
        if(task.getPetition() != null && !task.getPetition().isEmpty() && //Doesn't get a field petition null or void
                task.getStatus() != null && !task.getStatus().isEmpty()){ //Doesn't get a field status null or void.
            //If all the validations are met, we add the task to the task-list.
            // Convertir DTO a entidad antes de guardar
            TaskEntity taskEntity = TaskMapper.toEntity(task);

            // Guardar entidad en la base de datos
            TaskEntity savedTask = taskRepository.save(taskEntity);

            TaskEntity newTask = taskRepository.findFirstByOrderByIdDesc();

            // Convertir la entidad guardada nuevamente a DTO
            TaskDto taskDto = TaskMapper.toDTO(newTask);

            return taskDto;
        }
        return null;
    }

    @Override
    public TaskDto putTask(TaskDto task, int id) {
        if (task.getPetition() != null && !task.getPetition().isEmpty() &&
                task.getStatus() != null && !task.getStatus().isEmpty()) {

            Optional<TaskEntity> taskOptional = taskRepository.findById((long) id);
            if (taskOptional.isPresent()) {
                TaskEntity existingTask = taskOptional.get();

                // Actualizar los campos
                existingTask.setPetition(task.getPetition());
                existingTask.setStatus(task.getStatus());

                // Guardar los cambios (esto sí hace UPDATE)
                TaskEntity updatedTask = taskRepository.save(existingTask);

                return TaskMapper.toDTO(updatedTask);
            }
        }
        return null;
    }

    @Override
    public TaskDto patchTask(TaskDto task, int id) {
        Optional<TaskEntity> taskOptional = taskRepository.findById((long) id);
        if (taskOptional.isPresent()) {
            TaskEntity existingTask = taskOptional.get();

            if(task.getPetition() != null && !task.getPetition().isEmpty()){
                existingTask.setPetition(task.getPetition());
            }
            if(task.getStatus() != null && !task.getStatus().isEmpty()){
                existingTask.setStatus(task.getStatus());
            }

            // Guardar los cambios (esto sí hace UPDATE)
            TaskEntity updatedTask = taskRepository.save(existingTask);

            return TaskMapper.toDTO(updatedTask);
        }
        return null;
    }

    @Override
    public boolean deleteTask(int id) {
        if(taskRepository.existsById((long) id)){
            taskRepository.deleteById((long) id);
            return true;
        }
        return false;
    }
}
