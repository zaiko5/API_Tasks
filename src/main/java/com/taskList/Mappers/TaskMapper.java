package com.taskList.Mappers;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;

//Clase que controla el como se cambia de dto a entidad y viceversa
public class TaskMapper {
    //de entidad (parametro) a Dto (return)
    public static TaskDto toDTO(TaskEntity entity) {
        return new TaskDto(entity.getId(), entity.getPetition(), entity.getStatus());
    }

    //De dto (parametro) a entidad (retorno)
    public static TaskEntity toEntity(TaskDto  dto) {
        return new TaskEntity(dto.getId(), dto.getPetition(), dto.getStatus());
    }
}
