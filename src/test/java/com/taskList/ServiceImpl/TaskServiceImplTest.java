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
//AQUI SE ESTÄ USANDO MOCKS

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

    //Caso 2: Excepcion, no hay nada en la lista.
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
     * Testeos al servicio getTaskID
     */
    //Caso 1: Retorno de objeto exitoso, si hay usuario con x id.
    @Test
    void getTaskID() {
        //ARRANGE
        //Solo necesitamos una tarea valida que es la que se va a retornar
        TaskEntity tareaMockeada = new TaskEntity(1, "Tarea 1", "To do");

        //Definiendo lo que se debe de retornar cuando se llame a la funcion del repositorio (en este caso, la tarea valida"
        when(taskRepository.findById((long) tareaMockeada.getId())).thenReturn(Optional.of(tareaMockeada));

        //ACT
        //Generando el resultado llamando a la funcion para comparar.
        TaskDto resultado = taskService.getTaskID(tareaMockeada.getId());

        //ASSERT
        //Validaciones del resultado, lo que deberia pasar en este caso
        assertNotNull(resultado); //El resultado no debe de ser nulo
        assertEquals("Tarea 1", resultado.getPetition()); //El titulo de la tarea deberia de ser tarea 1.
    }

    //Caso 2: No se encuentra el id (HAY EXCEPCION)
    @Test
    void getTaskID_throwException() {
        // ARRANGE
        int tareaId = 999; // ID de tarea que no existe.

        //Definiendo que es lo que se deberia de retornar en este caso específico (un Optional.empty, la funcion findById siempre retorna un optional)
        when(taskRepository.findById((long) tareaId)).thenReturn(Optional.empty());

        //ACT + ASSERT
        //Verificando que  lanza la excepcion del tipo tareaNoEncontrada cuando se llame a la funcion getTaskId con este id.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.getTaskID(tareaId));
    }

    /**
     * Testeo del servicio postTask
     */
    //Caso 1: Ingreso de tarea correcto y un save correcto en la base de datos.
    @Test
    void postTask() {
        //Arrange
        TaskDto dtoEntrada = new TaskDto(0,"Estudiar Spring", "To do"); //DTO de entrada que es el que el usuario deberia de ingresar
        TaskEntity entidadEntrada = new TaskEntity(0, "Estudiar Spring", "To do"); //Entidad de entrada que es el mismo DTO del usuario pero convertido a entidad.
        TaskEntity entidadGuardada = new TaskEntity(1, "Estudiar Spring", "To do"); //Entidad de salida que es la misma entidad pero ahora con ID
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Spring", "To do"); //DTO de salida que es la misma entidad de salida..

        //Definiendo que es lo que debe retornar la llamada al metodo JPA (save) con el mismo objeto que se usa de parametro en su llamada desde la clase de servicio, en este caso, debe de retornar la entidadGuardada.
        when(taskRepository.save(entidadEntrada)).thenReturn(entidadGuardada);

        //ACT
        //Obteniendo el resultado de la funcion con el dtoEntrada, que es lo que se pide en el metodo original de parametro.
        TaskDto resultado = taskService.postTask(dtoEntrada);

        //ASSERT
        assertEquals(dtoEsperado.getId(), resultado.getId()); //Definiendo que el id del dtoEsperado debe ser igual al del resultado.
        assertEquals(dtoEsperado.getPetition(), resultado.getPetition()); //La peticion del DTO esperado debe ser la misma que la del resultado
        assertEquals(dtoEsperado.getStatus(), resultado.getStatus()); //El status del dto esperado debe ser el mismo que el de la llamada a la funcion.
        assertNotNull(resultado); //El resultado de la funcion no debe ser nulo
    }

    //Caso 2: Se ingresa uno de los datos en null o vacios y da excepcion.
    @Test
    void postTask_throwException(){
        //ARRANGE
        TaskDto dtoEntrada = new TaskDto(0,"", null); //Solo necesitamos el objeto de entrada con datos vacios o nulos.

        //ACT + ASSERT
        //Al no haber llamado a una funcion JPA antes de la validacion la cual no pasó nuestro objeto, no se usa el when...thenReturn..., y se pasa directo a definir que se debio haber tirado una excepcion al haber llamado al metodo con el dtoEntrada.
        assertThrows(CamposFaltantesException.class, () -> taskService.postTask(dtoEntrada));
    }

    /**
     * Testeo de la funcion putTask
     */
    //Caso 1: Se recibe un objeto con los campos correctos, existente en la DB y no lanza excepciones.
    @Test
    void putTask() {
        //ARRANGE
        TaskDto dtoEntrada = new TaskDto(1, "Estudiar Spring", "In Progress");  //DTO de entrada.
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");  //Entidad que debera de retornar el metodo find ya existente en la DDBB.
        TaskEntity tareaActualizada = new TaskEntity(1, "Estudiar Spring", "In Progress");  //La entidad con los datos actualizados.
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Spring", "In Progress");  //El DTO que deberia de regresarse al controlador.

        //Definiendo que deberia retornar en este caso cada uno de los llamados a la BD JPA
        //En findById con el id de la tarea de tarea digitada, deberia de retornar la entidad existente en la DB.
        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        //Cuando se guarda la tarea existente, que es a la que se le hacen los cambios, deberia de retornar la entidad actualizada, que deberia de ser igual al DTO de entrada.
        when(taskRepository.save(tareaExistente)).thenReturn(tareaActualizada);

        //ACT
        //Obteniendo el resultado llamando al servicio.
        TaskDto resultado = taskService.putTask(dtoEntrada, 1);

        //ASSERT
        assertNotNull(resultado);  // El resultado no debe ser nulo
        assertEquals(dtoEsperado.getId(), resultado.getId());  // Verifica que el id sea el mismo
        assertEquals(dtoEsperado.getPetition(), resultado.getPetition());  // Verifica que el campo petition sea el mismo
        assertEquals(dtoEsperado.getStatus(), resultado.getStatus());  // Verifica que el campo status sea el mismo
    }

    //Caso 2: Alguno de los campos es vacios o nulos y tiran una excepcion.
    @Test
    void putTask_throwExceptionNullField(){
        //ARRANGE
        TaskDto dtoEntrada = new TaskDto(1, "", null);  //DTO de entrada (unico a usar ya que hasta ahi llega la logica antes de la primera validacion.

        //ACT + ASSERT
        //Verificando que se lance la excepcion campos faltantes enseguida que se llame a la funcion
        assertThrows(CamposFaltantesException.class, () -> taskService.putTask(dtoEntrada, 1));
    }

    //Caso 3: Los campos son correctos pero el id no se encuentra entonces lanza una excepcion.
    @Test
    void putTask_throwExceptionTaskNotFind(){
        //ARRANGE
        TaskDto dtoEntrada = new TaskDto(1, "Estudiar Spring", "In Progress");  //DTO de entrada.

        //Definiendo que es lo que deberia de retornar la llamada a la funcion findById con x id, en este caso, suponiendo que la tarea no se encuentra, haremos que lance la un optional empty.
        when(taskRepository.findById((long) 1)).thenReturn(Optional.empty());

        //ACT + ASSERT
        //Verificando que se lanza la excepcion tarea no encontrada cuando se llama a la funcion putTask.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.putTask(dtoEntrada, 1));
    }

    /**
     * Testeo de la funcion pathTask.
     */
    //Caso 1 y 1.1: Se mandan tareas incompletas pero con id correcto, entonces no se lanza una excepcion.
    @Test
    void patchTask() {
        //Arrange
        //Caso 1: Peticion null pero status correcto
        TaskDto dtoEntrada = new TaskDto(1, null, "In Progress");  // DTO de entrada
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do");  //Entidad ya registrada en la BD
        TaskEntity tareaActualizada = new TaskEntity(1, "Estudiar Java", "In Progress");  //Entidad actualizada y registrada en la DB
        TaskDto dtoEsperado = new TaskDto(1, "Estudiar Java", "In Progress");  //DTO de salida

        //Caso 1.1: peticion correcta pero status null.
        TaskDto dtoEntrada2 = new TaskDto(2, "Hacer algo mas", null);  //DTO de entrada
        TaskEntity tareaExistente2 = new TaskEntity(2, "Estudiar Java", "To Do");  //Entidad existente en la BD
        TaskEntity tareaActualizada2 = new TaskEntity(2, "Hacer algo mas", "To Do");  //Entidad actualizada y registrada en la BD.
        TaskDto dtoEsperado2 = new TaskDto(2, "Hacer algo mas", "To Do");  //DTO de salida.

        //Caso 1. Definiendo lo que deberia de retornar las funciones de JPA para la tarea 1
        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        when(taskRepository.save(tareaExistente)).thenReturn(tareaActualizada);

        //Caso 1.1: Definiendo lo que deberia de retornar las funciones deJPa para la tarea 1.1
        when(taskRepository.findById((long) 2)).thenReturn(Optional.of(tareaExistente2));
        when(taskRepository.save(tareaExistente2)).thenReturn(tareaActualizada2);

        //ACT
        TaskDto resultado = taskService.patchTask(dtoEntrada, 1); //Resultado 1
        TaskDto resultado2 = taskService.patchTask(dtoEntrada2, 2); //Resultado 1.1

        //Asserts caso 1
        assertNotNull(resultado);
        assertEquals(resultado.getId(), dtoEntrada.getId());
        assertEquals(resultado.getStatus(), dtoEntrada.getStatus());
        assertNotEquals(resultado.getPetition(),dtoEntrada.getPetition());
        assertEquals(resultado.getPetition(), tareaExistente.getPetition());

        //ASSERTS caso 1.1
        assertEquals(resultado2.getId(), dtoEntrada2.getId());
        assertNotEquals(resultado2.getStatus(), dtoEntrada2.getStatus());
        assertEquals(resultado2.getPetition(),dtoEntrada2.getPetition());
        assertEquals(resultado2.getPetition(), tareaExistente2.getPetition());
    }

    //Caso 2: El id es incorrecto y se lanza una excepcion
    @Test
    void patchTask_throwException(){
        //ARRANGE
        TaskDto dtoEntrada = new TaskDto(1, null, "In Progress");  //DTO de entrada

        //Al haber un llamado a la funcion de JPA findById antes de la verificacion del id en el servicio, se define que es lo que deberia de regresar en este caso, deberia ser un optional.empty
        when(taskRepository.findById((long) 1)).thenReturn(Optional.empty());

        //ASSERT + ACT
        //Verificando que se debe de lanzar una excepcion despues de llamar a la funcion JPA, habiendo llamado al servicio Patch con id incorrecto.
        assertThrows(TareaNoEncontradaException.class, () -> taskService.patchTask(dtoEntrada, 1));
    }

    //Caso 3: id correcto pero ambos campos nulos (segunda y tercera verificacion) (no lanza excepcion)
    @Test
    void patchTask_noChangesIfAllFieldsAreNullOrEmpty() {
        TaskDto dtoEntrada = new TaskDto(1, "", null);  //DTO de entrada
        TaskEntity tareaExistente = new TaskEntity(1, "Estudiar Java", "To Do"); //Entidad encontrada en la BD

        //Definiendo que es lo que se debe de retornar en ambas llamadas al JPa, en el primer caso al si existir la tarea con el id ingresado deberia de retornar la tarea existente.
        when(taskRepository.findById((long) 1)).thenReturn(Optional.of(tareaExistente));
        //En la segunda llamada con save, deberia retornar la msima tarea existente,ya que no hay cambios para modificar.
        when(taskRepository.save(tareaExistente)).thenReturn(tareaExistente);

        //ACT
        //Obteniendo el resultado de llamar a patchTask con la tarea vacia.
        TaskDto resultado = taskService.patchTask(dtoEntrada, 1);

        //ASSERT
        assertEquals("Estudiar Java", resultado.getPetition()); //El titulo del resultado deberia ser el mismo de la entidad existente
        assertEquals("To Do", resultado.getStatus()); //El estado del resultado deberia ser el mismo de la entidad existente.
        assertNotNull(resultado); //El resultado no deberia de ser null
    }

    /**
     * Testeo de la funcion deleteTask
     */
    //Caso 1: El delete sale bien, la tarea pasada existe y no hay excepciones.
    @Test
    void deleteTask() {
        //ASSERT.
        int id = 1; //Solo ocupamos el id de entrada

        //Definir que es lo que se va a retornar cuando se llame a la funcion existsById con el id pasado, en este caso debe ser un true de que si existe.
        when(taskRepository.existsById((long) id)).thenReturn(true);
        //Definir que es lo que debe de pasar cuando se llame a la funcion deleteById con el id pasado, sera un doNothing ya que solo se debe de eliminar.
        doNothing().when(taskRepository).deleteById((long) id);

        //ACT
        //Llamamos a la funcion deleteTask del servicio con el id
        taskService.deleteTask(id);

        //ASSERT
        //Al no retornar nada, solo se vericia que se haya llamado al metodo deltebyId.
        verify(taskRepository).deleteById((long) id);
    }

    //Caso 2: el id no existe y lanza una excepcion del tipo tarea no encontrada
    @Test
    void deleteTask_ThrowException() {
        //ARRANGE
        int id = 1; //Solo se necesita un id de entrada.

        //Definir que es lo que se va a retornar cuando se llame a la funcion existsById con el id pasado, y en este caso sera false simulando que la tarea no se encontro.
        when(taskRepository.existsById((long) id)).thenReturn(false);

        //ACT + ASSERT
        assertThrows(TareaNoEncontradaException.class, () -> taskService.deleteTask(id)); //Verificar que se lanza la excepcion cuando se llama al metodo.
        verify(taskRepository, never()).deleteById(anyLong()); //Verificar que el repositorio nunca llamó al metodo deleteById.
    }
}