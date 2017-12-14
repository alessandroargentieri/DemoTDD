package com.quicktutorialz.learnmicroservices.DemoTDD.daos;

import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ToDoDao extends JpaRepository<ToDo, Integer> {
    List<ToDo> findByFkUser(String email);
}
