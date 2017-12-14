package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import com.quicktutorialz.learnmicroservices.DemoTDD.DemoTddApplication;
import com.quicktutorialz.learnmicroservices.DemoTDD.daos.ToDoDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.daos.UserDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DemoTddApplication.class)  //load configuration file
@RunWith(SpringRunner.class)
public class ToDoServiceImplTest {

    @Autowired @InjectMocks
    ToDoService toDoService;

    @Mock
    ToDoDao toDoDao;

    //test che il toDoService contenga l'istanza di toDoDao
    @Test
    public void ContainsToDoDao() throws Exception{
        assertNotNull(ReflectionUtils.getPrivateField(toDoService, "toDoDao"));
        assertTrue(ReflectionUtils.getPrivateField(toDoService, "toDoDao") instanceof ToDoDao);

    }





    ////////////////


    //al service viene chiesta la lista dei ToDos
    @Test
    public void showToDos(){
        List<ToDo> expectedList = new ArrayList<>();
        expectedList.add(new ToDo(1, "doing things", new Date(), "high", "alex@quicktutorialz.com"));

        when(toDoDao.findByFkUser("alex@quicktutorialz.com")).thenReturn(expectedList);
        assertEquals(expectedList, toDoService.findToDosByEmail("alex@quicktutorialz.com"));
    }


    //al service viene chiesto di salvare un ToDos
    @Test
    public void saveToDo(){
        ToDo toDoToBeSaved = new ToDo(1, "doing things", new Date(), "high", "alex@quicktutorialz.com");
        when(toDoDao.save(toDoToBeSaved)).thenReturn(toDoToBeSaved);
        assertEquals(toDoToBeSaved, toDoService.saveToDo(toDoToBeSaved));

    }


    //al service viene chiesto di eliminare un ToDos
    @Test
    public void deleteToDo(){
        toDoService.deleteToDo(1);
        verify(toDoDao).delete(1);
    }

}