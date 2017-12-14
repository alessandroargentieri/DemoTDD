package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ToDoService {

    List<ToDo> findToDosByEmail(String email);

    ToDo saveToDo(ToDo toDo);

    void deleteToDo(int id);
}
