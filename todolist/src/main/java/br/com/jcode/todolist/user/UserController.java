package br.com.jcode.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserRepository userRepository;

    @PostMapping("/")
    public ResponseEntity create(@RequestBody UserModel UserModel){

           var user = this.userRepository.findByUsername(UserModel.getUsername());

           if(user != null){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe, gentileza cadastrar um usuário não existente.");
              
        }

        var passwordCrypt = BCrypt.withDefaults().hashToString(12, UserModel.getPassword().toCharArray());
        UserModel.setPassword(passwordCrypt);

         var userCreated = this.userRepository.save(UserModel);

        return  ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
             
           

    }
    
}
