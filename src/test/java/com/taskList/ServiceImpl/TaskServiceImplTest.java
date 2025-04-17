package com.taskList.ServiceImpl;

import com.taskList.DTOs.TaskDto;
import com.taskList.Entities.TaskEntity;
import com.taskList.Exception.CamposFaltantesException;
import com.taskList.Exception.ListaVaciaException;
import com.taskList.Exception.TareaNoEncontradaException;
import com.taskList.Repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.scheduling.config.Task;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceImplTest {
//Falta agregar comentarios y aparte agregar algunas notas en notion

    @Mock //Simulando la base de datos y agregando las dependencias del servicio a testear (las mismas).
    TaskRepository taskRepository;

    @InjectMocks //Inyectando el servicio a testear (en este caso se están testeando las funciones de la clase de servicio).
    TaskServiceImpl taskService;

    @BeforeEach //Inicializando los mocks con beforeEach y una funcion setUp.
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Testeo al servicio getTasks.
     */
    //Caso 1: Retorno de datos exitosos.
    @Test
    void getTasks() {
        //ARRANGE
        //Datos de entrada, una lista de tareas la cual se va a retornar.
        List<TaskEntity> tareasMockeadas = List.of(
                new TaskEntity(1, "Tarea 1", "Done"),
                new TaskEntity(2, "Tarea 2", "To do")
        );

        //Definiendo que es lo que se debe de retornar cuando se llame a la funcion de la base de datos, en este caso, findAll, esto se debe de hacer con cada funcion de JPA que haya en el metodo, cuando se llame a la funcion, se tiene que retornar la lista que ya existe.
        when(taskRepository.findAll()).thenReturn(tareasMockeadas);

        //ACT
        //Llamando a la funcion, el paso de Act y Assert se hace separado cuando no hay excepcion, el resultado debe ser el llamado a la funcion que se está inyectando, en este caso, la del servicio.
        List<TaskDto> resultado = taskService.getTasks();

        //ASSERT
        //Verificando que todo salga bien con el resultado.
        assertNotNull(resultado); //El resultado no deberia ser null
        assertEquals(2, resultado.size()); //El tamaño de la lista deberia de ser 2 (en este caso específico)
        assertEquals("Tarea 2", resultado.get(1).getPetition()); //El titulo de el elemento 2 deberia ser "Tarea 2" EN ESTE CASO ESPECÏFICO.
    }

    //Test para el caso de la excepcion.
    @Test
    void getTasks_throwException(){
        //ARRANGE
        //En este caso necesitamos una lista vacia para provocar la excepcion.
        List<TaskEntity> tareasMockeadas = List.of(); //Lista vacía (no hay tareas)

        //Esto es parte del arrange aun, usamos este when ya que sí o sí se va a ejecutar el findAll, no hay una condicion antes de el llamado al metodo findAll que haga que no se ejecute, cuando se llame al metodo deberia de retornar la lista, haya tareas o no.
        when(taskRepository.findAll()).thenReturn(tareasMockeadas);

        //ACT + ASSERT
        //Teniendo en cuenta que despues del llamado hay una condicion que dice que la lista no deberia de estar vacía, pues no se cumple la condicion y se salta directamente a la excepcion, verificamos que se lanza una excepcion del tipo ListaVaciaException al llamar a la funcion taskService.getTask.
        assertThrows(ListaVaciaException.class, () -> taskService.getTasks());
    }

    /**
     * 
     */
    @Test
    void getTaskID() {
        //Datos de entrada.
        TaskEntity tareaMockeada = new TaskEntity(1, "Tarea 1", "To do");

        when(taskRepository.findById((long) tareaMockeada.getId())).thenReturn(Optional.of(tareaMockeada));

        TaskDto resultado = taskService.getTaskID(tareaMockeada.getId());

        assertNotNull(resultado);
        assertEquals("Tarea 1", resultado.getPetition());
    }

    @Test
    void getTaskID_throwException() {
        // Datos de entrada.
        int tareaId = 999; // ID de tarea que no existe.

        // Simulando que no se encuentra la tarea.
        when(taskRepository.findById((long) tareaId)).thenReturn(Optional.empty());

        // Verificando que se lance la excepción.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.getTaskID(tareaId));
    }

    @Test
    void postTask() {
        //Arrange
        TaskDto dtoEntrada = new TaskDto(0,"Estudiar Spring", "To do"); //Ejecucion metodo
        TaskEntity entidadEntrada = new TaskEntity(0, "Estudiar Spring", "To do"); //Para la comparacion
        TaskEntity entidadGuardada = new TaskEntity(1, "Estudiar Spring", "To do"); //Retorno en la comparacion
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Spring", "To do"); //Para la comparacion de asserts.

        //Definiendo que es lo que debe retornar el metodo con un valor x.
        when(taskRepository.save(entidadEntrada)).thenReturn(entidadGuardada);

        //Act
        TaskDto resultado = taskService.postTask(dtoEntrada); //Llamando a la funcion para obtener el resultado

        //Assert
        assertEquals(dtoEsperado.getId(), resultado.getId()); //Definiendo que el id del dtoEsperado debe ser igual al del resultado.
        assertEquals(dtoEsperado.getPetition(), resultado.getPetition());
        assertEquals(dtoEsperado.getStatus(), resultado.getStatus());
        assertNotNull(resultado);
    }

    @Test
    void postTask_throwException(){
        //Arrange
        TaskDto dtoEntrada = new TaskDto(0,"", "null");

        // Act + Assert
        assertThrows(CamposFaltantesException.class, () -> taskService.postTask(dtoEntrada));
    }

    @Test
    void putTask() {
        //Arrange
        TaskDto dtoEntrada = new TaskDto(1, "Estudiar Spring", "In Progress");  // DTO que vamos a actualizar
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");  // La tarea que ya existe en la base de datos
        TaskEntity tareaActualizada = new TaskEntity(1, "Estudiar Spring", "In Progress");  // La tarea con los nuevos datos
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Spring", "In Progress");  // El DTO que esperamos como resultado

        // Configurar el repositorio mockeado
        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        when(taskRepository.save(tareaExistente)).thenReturn(tareaActualizada);

        TaskDto resultado = taskService.putTask(dtoEntrada, 1); //Llamando al metodo (Act)

        // Assert
        assertNotNull(resultado);  // El resultado no debe ser nulo
        assertEquals(dtoEsperado.getId(), resultado.getId());  // Verifica que el id sea el mismo
        assertEquals(dtoEsperado.getPetition(), resultado.getPetition());  // Verifica que el campo petition sea el mismo
        assertEquals(dtoEsperado.getStatus(), resultado.getStatus());  // Verifica que el campo status sea el mismo
    }

    @Test
    void putTask_throwExceptionNullField(){
        TaskDto dtoEntrada = new TaskDto(1, "", null);  // DTO que vamos a actualizar
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");  // La tarea que ya existe en la base de datos

        //Haciendo act + assert ya que la condicion no da chance a que el repositorio haga un llamado, si no que manda directo a la excepcion
        assertThrows(CamposFaltantesException.class, () -> taskService.putTask(dtoEntrada, 1));
    }

    @Test
    void putTask_throwExceptionTaskNotFind(){
        TaskDto dtoEntrada = new TaskDto(1, "Estudiar Spring", "In Progress");  // DTO que vamos a actualizar

        //Definiendo que cuando se haga la busqueda del id y sea vacio pasará lo siguiente
        when(taskRepository.findById((long) 1)).thenReturn(Optional.empty());

        // Act + Assert
        //Se lanzará una excepcion del tipo tara no encontrada llamando a la funcion testeada.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.putTask(dtoEntrada, 1));
    }

    @Test
    void patchTask() {
        //Arrange
        TaskDto dtoEntrada = new TaskDto(1, null, "In Progress");  // DTO que vamos a actualizar
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");  // La tarea que ya existe en la base de datos
        TaskEntity tareaActualizada = new TaskEntity(1, "Estudiar Java", "In Progress");  // La tarea con los nuevos datos
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Java", "In Progress");  // El DTO que esperamos como resultado

        TaskDto dtoEntrada2 = new TaskDto(2, "Hacer algo mas", null);  // DTO que vamos a actualizar
        TaskEntity tareaExistente2 = new TaskEntity(2, "Estudiar Java", "To Do");  // La tarea que ya existe en la base de datos
        TaskEntity tareaActualizada2 = new TaskEntity(2, "Hacer algo mas", "To Do");  // La tarea con los nuevos datos
        TaskDto dtoEsperado2 = new TaskDto(2, "Hacer algo mas", "To Do");  // El DTO que esperamos como resultado

        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        when(taskRepository.save(tareaExistente)).thenReturn(tareaActualizada);

        when(taskRepository.findById((long) 2)).thenReturn(Optional.of(tareaExistente2));
        when(taskRepository.save(tareaExistente2)).thenReturn(tareaActualizada2);

        TaskDto resultado = taskService.patchTask(dtoEntrada, 1);
        TaskDto resultado2 = taskService.patchTask(dtoEntrada2, 2);

        //Asserts
        assertNotNull(resultado);  // El resultado no debe ser nulo
        assertEquals(resultado.getId(), dtoEntrada.getId());
        assertEquals(resultado.getStatus(), dtoEntrada.getStatus());
        assertNotEquals(resultado.getPetition(),dtoEntrada.getPetition());
        assertEquals(resultado.getPetition(), tareaExistente.getPetition());

        assertEquals(resultado2.getId(), dtoEntrada2.getId());
        assertNotEquals(resultado2.getStatus(), dtoEntrada2.getStatus());
        assertEquals(resultado2.getPetition(),dtoEntrada2.getPetition());
        assertEquals(resultado2.getPetition(), tareaExistente2.getPetition());
    }

    @Test
    void patchTask_throwException(){
        TaskDto dtoEntrada = new TaskDto(1, null, "In Progress");  // DTO que vamos a actualizar

        when(taskRepository.findById((long) 1)).thenReturn(Optional.empty());

        assertThrows(TareaNoEncontradaException.class, () -> taskService.patchTask(dtoEntrada, 1));
    }

    @Test
    void patchTask_noChangesIfAllFieldsAreNullOrEmpty() {
        TaskDto dtoEntrada = new TaskDto(1, "", null);  // Todos vacíos
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");

        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        when(taskRepository.save(tareaExistente)).thenReturn(tareaExistente);

        TaskDto resultado = taskService.patchTask(dtoEntrada, 1);

        assertEquals("Estudiar Java", resultado.getPetition());
        assertEquals("To Do", resultado.getStatus());
    }

    @Test
    void deleteTask() {
        int id = 1;

        when(taskRepository.existsById((long) id)).thenReturn(true);
        doNothing().when(taskRepository).deleteById((long) id);

        taskService.deleteTask(id);
        verify(taskRepository).deleteById((long) id); //Verifica que se llamó al método
    }

    @Test
    void deleteTask_ThrowException() {
        int id = 1;

        when(taskRepository.existsById((long) id)).thenReturn(false);

        assertThrows(TareaNoEncontradaException.class, () -> taskService.deleteTask(id));
        verify(taskRepository, never()).deleteById(anyLong());
    }
}