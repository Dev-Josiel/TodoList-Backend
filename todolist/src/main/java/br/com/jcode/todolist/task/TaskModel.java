package br.com.jcode.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;


@Data
@Entity(name = "tb_tasks")
public class TaskModel {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;
    private String description;

    @Column(length = 50)
    private String title;
    private LocalDateTime startAT;
    private LocalDateTime endAT;
    private String priority;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private UUID idUser;

    public void setTitle(String title) throws Exception{
        if(title.length() > 50){
          throw new Exception("O Campo title deve conter no máximo 50 caracteres");
        }
        this.title = title;
    }
    
}
