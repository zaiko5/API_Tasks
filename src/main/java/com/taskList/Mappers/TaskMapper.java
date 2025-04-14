package com.taskList.Mappers;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;

public class TaskMapper {
    public static TaskDto toDTO(TaskEntity entity) {
        return new TaskDto(entity.getId(), entity.getPetition(), entity.getStatus());
    }

    public static TaskEntity toEntity(TaskDto  dto) {
        return new TaskEntity(dto.getId(), dto.getPetition(), dto.getStatus());
    }
}
