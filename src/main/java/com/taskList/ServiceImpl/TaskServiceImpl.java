package com.taskList.ServiceImpl;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;
import com.taskList.Exception.CamposFaltantesException;
import com.taskList.Exception.ListaVaciaException;
import com.taskList.Exception.TareaNoEncontradaException;
import com.taskList.Mappers.TaskMapper;
import com.taskList.Repository.TaskRepository;
import com.taskList.Services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//Implementacion de la interfaz servicio.
@Service //Definir que es un servicio.
public class TaskServiceImpl implements TaskService {

    @Autowired //Inyeccion de dependencias de un repositorio que implementa JPA (funciones para base de datos).
    @Lazy //Definir que este repositorio no se implementará hasta que sea necesario.
    TaskRepository taskRepository; //Inyeccion de dependencias del tipo taskrepository.

    /**
     * Peticion get para obtener todas las tareas
     * @return una lista del tipo dto (presentar la informacion) con todas las tareas, o en su caso, se lanza una excepcion del tipo ListaVaciaException capturada desde el Handler.
     */
    @Override
    public List<TaskDto> getTasks() {
        List<TaskEntity> entities = taskRepository.findAll(); //Funcion que retorna todo de una tabla (select * from x)
        ArrayList<TaskDto> dtos = new ArrayList<>(); //Creando una lista de dtos para cada una de las entidades encontradas anteriormente
        for (TaskEntity entity : entities) {
            dtos.add(TaskMapper.toDTO(entity)); //Agregamos a la lista dto la entidad transformada a dto.
        }
        if(dtos.isEmpty()){ //Si la lista es una lista vacia.
            throw new ListaVaciaException(); //Se tira la excepcion del tipo listaVaciaException.
        }
        return dtos; //Retornamos la lista de dtos si no esta vacia.
    }

    /**
     * Peticion get para retornar una tarea especifica.
     * @param id pasado por path desde el getMapping
     * @return un objeto dto para mostrar o en su caso, se lanza una excepcion personalizada capturada desde el handle.
     */
    @Override
    public TaskDto getTaskID(int id) {
        Optional<TaskEntity> taskOptional = taskRepository.findById((long) id); //Creando un objeto optional del tipo taskEntity donde se guarda lo encontrado con findById (se le pasa la id en long)
        if (taskOptional.isPresent()) { //Verificando si la tarea existe o no
            TaskEntity task = taskOptional.get(); //Abstrayendo la entidad a un objeto entidad
            TaskDto taskDto = TaskMapper.toDTO(task); //Transformando a un objeto dto.
            return taskDto; //Retornamos la tarea en dto.
        }
        throw new TareaNoEncontradaException(id); //Lanzamos la excepcion y le pasamos al constructor el id de la tarea no encontrada.
    }

    /**
     * Peticion post para agregar un registro a la BBDD.
     * @param task pasado por JSON
     * @return el objeto DTO o en su caso null
     */
    @Override
    public TaskDto postTask(TaskDto task) {
        //Verificando que los campos de el JSON no esten vacios.
        if(task.getPetition() != null && !task.getPetition().isEmpty() && //Doesn't get a field petition null or void
                task.getStatus() != null && !task.getStatus().isEmpty()){ //Doesn't get a field status null or void.
            //If all the validations are met, we add the task to the task-list.

            // Convertir DTO, es DTO ya que viene del front, a entidad antes de guardar, ya que en el back solo se pueden guardar entidades.
            TaskEntity taskEntity = TaskMapper.toEntity(task);

            // Guardar entidad en la base de datos
            TaskEntity savedTask = taskRepository.save(taskEntity);

            // Convertir la entidad guardada a DTO para exponerla en el front.
            TaskDto taskDto = TaskMapper.toDTO(savedTask);

            return taskDto; //Retornamos la entidad convertida a DTO
        }
        throw new CamposFaltantesException(); //Lanzamos una excepcion del tipo campos faltantes.
    }

    /**
     * Peticion put para modificar totalmente un registro.
     * @param task pasado por JSON
     * @param id pasado por Path
     * @return El objeto DTO modificado o en su caso un null
     */
    @Override
    public TaskDto putTask(TaskDto task, int id) {
        //Verificando que los campos no sean null o vacios
        if (task.getPetition() != null && !task.getPetition().isEmpty() &&
                task.getStatus() != null && !task.getStatus().isEmpty()) {

            //Encontramos la tarea a modificar por el id, creada en un Optional ya que viene de findById y si existe...
            Optional<TaskEntity> taskOptional = taskRepository.findById((long) id);
            if (taskOptional.isPresent()) {
                //Pasamos la tarea de tipo Optional a una entidad con el metodo get, teniendo en cuenta que en la task optional ya tenemos el id.
                TaskEntity existingTask = taskOptional.get();

                // Actualizar los campos en la tarea entidad desde los campos del dto.
                existingTask.setPetition(task.getPetition());
                existingTask.setStatus(task.getStatus());

                // Guardar los cambios
                TaskEntity updatedTask = taskRepository.save(existingTask);

                return TaskMapper.toDTO(updatedTask); //Retornamos la tarea actualizada transformada a DTO
            }
        }
        return null;
    }

    /**
     * Peticion path para modificar parcialmente una tarea
     * @param task pasado por JSON
     * @param id pasado por poth
     * @return El objeto modificado o en su caso null
     */
    @Override
    public TaskDto patchTask(TaskDto task, int id) {
        //Verificamos si existe o no el usuario con tal id con findBNyId
        Optional<TaskEntity> taskOptional = taskRepository.findById((long) id);
        if (taskOptional.isPresent()) { //Si el usuario existe
            //Pasamos el optional a un usuario entidad para pasarlo a la DB.
            TaskEntity existingTask = taskOptional.get();

            //Verificamos que los campos no sea nulos y donde no lo sean
            if(task.getPetition() != null && !task.getPetition().isEmpty()){
                existingTask.setPetition(task.getPetition()); //Le cambiamos el campo al que haya pasado el usuario desde el JSON (DTO)
            }
            if(task.getStatus() != null && !task.getStatus().isEmpty()){
                existingTask.setStatus(task.getStatus());
            }

            // Guardar los cambios por el id que obtuvimos de findById
            TaskEntity updatedTask = taskRepository.save(existingTask);

            //Retornammos el objeto actualizado converido a DTO
            return TaskMapper.toDTO(updatedTask);
            /**
             * NOTA: Cuando se obtiene el optional de findById, si se obtiene todo el objeto pero los campos se cambian cuando usamos setTal con los valores de getTal (El objeto pasado DTO).
             */
        }
        return null;
    }

    /**
     * Peticion delete para eliminar un registro de la DB.
     * @param id pasado por path.
     * @return un booleano que dice si fue eliminao o no el valor.
     */
    @Override
    public boolean deleteTask(int id) {
        //Verificamos que exista o no la tarea con tal id
        if(taskRepository.existsById((long) id)){
            //Si existe usamos la funcion delete by id y la eliminamos segun el id.
            taskRepository.deleteById((long) id);
            //Retornamos true diciendo que la tarea fue eliminada correctamenete.
            return true;
        }
        //Retornamos false diciendo que la tarea no fue encontrada.
        return false;
    }
}
