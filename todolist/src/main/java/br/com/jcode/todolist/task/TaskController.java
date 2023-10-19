package br.com.jcode.todolist.task;


import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.jcode.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private ITaskRepository taskRepository;
     
    @PostMapping("/")
    public ResponseEntity create(@RequestBody TaskModel taskModel, HttpServletRequest request) {

        System.out.println("Chegou no controller: "+ request.getAttribute("idUser"));

        var idUser = request.getAttribute("idUser");
        taskModel.setIdUser((UUID) idUser);

        var currentdate = LocalDateTime.now();
        if(currentdate.isAfter(taskModel.getStartAT()) || currentdate.isAfter(taskModel.getEndAT()) ){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio e fim deve ser maior que a atual.");

        }
        if(taskModel.getStartAT().isAfter(taskModel.getEndAT()) ){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("A data de inicio deve ser menor que a data de término.");

        }

       var task =  this.taskRepository.save(taskModel);
       return ResponseEntity.status(HttpStatus.OK).body(task);
      


    }


    // METODO PARA LISTAR TODAS AS TAREFAS DO USUÁRIO AUTENTICADO.
    @GetMapping("/")
    public java.util.List<TaskModel> List(HttpServletRequest request){
         var idUser = request.getAttribute("idUser");
         var tasks = this.taskRepository.findByIdUser((UUID) idUser);

         return tasks;
    }


    //METODO PARA REALIZAÇÃO DE UPDATE DAS TAREFAS
    @PutMapping("/{id}")
    public ResponseEntity update(@RequestBody TaskModel taskModel, @PathVariable UUID id,HttpServletRequest request){
       

        var task = this.taskRepository.findById(id).orElse(null);
        var idUser = request.getAttribute("idUser");

        if(task == null){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa não encontrada.");
         }

        if(!task.getIdUser().equals(idUser)){
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não possui permissão para alterar essa tarefa.");
         }

        Utils.copyNonNullProperties(taskModel, task);

       
        var taskUpdated =  this.taskRepository.save(task);

        return ResponseEntity.ok().body(taskUpdated);

    }
    
}
