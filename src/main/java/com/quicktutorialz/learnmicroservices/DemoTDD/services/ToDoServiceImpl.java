package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import com.quicktutorialz.learnmicroservices.DemoTDD.daos.ToDoDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log
@Service
public class ToDoServiceImpl implements ToDoService {

    @Autowired
    ToDoDao toDoDao;

    @Override
    public List<ToDo> findToDosByEmail(String email){
        log.info("ToDoServiceImpl/findToDoByEmail");
        return toDoDao.findByFkUser(email);
    }

    @Override
    public ToDo saveToDo(ToDo toDo){
        return toDoDao.save(toDo);
    }

    @Override
    public void deleteToDo(int id){
        toDoDao.delete(id);
    }
}
