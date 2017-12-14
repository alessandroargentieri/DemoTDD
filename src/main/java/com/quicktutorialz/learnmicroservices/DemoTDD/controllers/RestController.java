package com.quicktutorialz.learnmicroservices.DemoTDD.controllers;


import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import com.quicktutorialz.learnmicroservices.DemoTDD.services.LoginService;
import com.quicktutorialz.learnmicroservices.DemoTDD.services.ToDoService;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JsonResponseBody;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.ToDoValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
@Log
@org.springframework.web.bind.annotation.RestController
public class RestController {

    @Autowired
    LoginService loginService;

    @Autowired
    ToDoService toDoService;


    @RequestMapping("/login")
    public ResponseEntity<JsonResponseBody> login(@RequestParam(name="email") String email, @RequestParam(name="password") String pwd) {
        try {
            String jwt = loginService.findUser(email, pwd);
            if(jwt != null){
                return ResponseEntity.status(HttpStatus.OK).header("jwt", jwt).body(new JsonResponseBody(HttpStatus.OK.value(), "Success! User logged in."));
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Wrong email or password."));
            }
        }catch(UnsupportedEncodingException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new JsonResponseBody(HttpStatus.FORBIDDEN.value(), "Wrong email or password."));
        }
    }


    @RequestMapping("/loginError")
    public ResponseEntity<JsonResponseBody> errorMessage(@RequestParam("error") String message){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Error: " + message));
    }


    @RequestMapping("/showToDos")
    public ResponseEntity<JsonResponseBody> showToDos(HttpServletRequest request){
        try {
            Map<String, Object> userData = (Map<String, Object>) request.getAttribute("userData");
            String email = (String) userData.get("email");
            List<ToDo> toDoList = toDoService.findToDosByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), toDoList));
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Error: " + e.toString()));
        }
    }


    @RequestMapping("/newToDo")
    public ResponseEntity<JsonResponseBody>  newToDo(@Valid ToDo toDo, BindingResult bindingResult){
        ToDoValidator validator = new ToDoValidator();
        validator.validate(toDo, bindingResult);
        if(bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Error: " + bindingResult.toString()));
        }
        try {
            ToDo savedToDo = toDoService.saveToDo(toDo);
            return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), savedToDo));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponseBody(HttpStatus.BAD_REQUEST.value(), "Error: " + e.toString()));
        }
    }


    @RequestMapping("/deleteToDo/{id}")
    public ResponseEntity<JsonResponseBody> deleteToDo(@PathVariable(name="id") int id){
        try {
            toDoService.deleteToDo(id);
        }catch (Exception e){
            log.warning(e.toString());
        }
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponseBody(HttpStatus.OK.value(), "ToDo deleted."));
    }
}
