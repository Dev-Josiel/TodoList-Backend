package br.com.jcode.todolist.filter;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.jcode.todolist.user.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class FilterTaskAuth  extends OncePerRequestFilter{  

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException { System.out.println("Entrou no filterTask ####################################");

      var servletPath = request.getServletPath();
      
      if(servletPath.startsWith("/tasks/")){


      

    //pegar autenticação usuario e senha
    var authorization = request.getHeader("Authorization");
    System.out.println("authorization --------------------------------------------------->>>>>>> "+authorization);
    var authEncode = authorization.substring("Basic".length()).trim();
   
     //INICIO --------->>>>     DECODIFICANDO BASE64

     byte[] authDecode = Base64.getDecoder().decode(authEncode);
     var authString = new String(authDecode);
     
     //FIM    --------->>>>     DECODIFICANDO BASE64

     String[] credencials = authString.split(":");

     String username =  credencials[0];
     String password =  credencials[1];

     //valida usuario
     var user =  this.userRepository.findByUsername(username);

     if(user == null){

        response.sendError(401, "Usuário sem autorização.");

      }else{

         //valida senha
        var passwordVerify =  BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        if(passwordVerify.verified){
              
              request.setAttribute("idUser", user.getId());

               filterChain.doFilter(request, response);
        }else{
               response.sendError(401, "Usuário sem autorização.");

        }
        

       


      }

}else{
         filterChain.doFilter(request, response);

}

        
 }

   
}
