package com.quicktutorialz.learnmicroservices.DemoTDD.controllers;

import com.quicktutorialz.learnmicroservices.DemoTDD.DemoTddApplication;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import com.quicktutorialz.learnmicroservices.DemoTDD.services.LoginService;
import com.quicktutorialz.learnmicroservices.DemoTDD.services.ToDoService;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JsonResponseBody;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JwtUtils;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//seconda classe di test ad essere generata

@SpringBootTest(classes = DemoTddApplication.class)  //load configuration file
@RunWith(SpringRunner.class)
public class RestControllerTest {

    @Autowired @InjectMocks
    RestController restController;

    @Mock
    LoginService loginService;

    @Mock
    ToDoService toDoService;


    //test che il controller restituisca una ResponseEntity<JsonResponseBody> in tutti i suoi metodi (punti di accesso)

    @Test
    public void ReturnsResponseEntityOfJsonResponseBody(){

        //later inserted
        BindingResult bindingResult = mock(BindingResult.class);
        HttpServletRequest request = mock(HttpServletRequest.class);

        assertThat(restController.login("alex@quicktutorialz.com", "Hello").getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.showToDos(request).getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.newToDo(new ToDo(1,"later inserted", new Date(), "high", "alex@quicktutorialz.com"), bindingResult).getBody(), instanceOf(JsonResponseBody.class));
        assertThat(restController.deleteToDo(1).getBody(), instanceOf(JsonResponseBody.class));
    }

    //test che il controller contenga il loginService e toDoService
    @Test
    public void ContainsServices()  throws Exception{
        assertNotNull(ReflectionUtils.getPrivateField(restController, "loginService"));
        assertTrue(ReflectionUtils.getPrivateField(restController, "loginService") instanceof LoginService);

        assertNotNull(ReflectionUtils.getPrivateField(restController, "toDoService"));
        assertTrue(ReflectionUtils.getPrivateField(restController, "toDoService") instanceof ToDoService);
    }

    //l'utente invia email e password al controller per l'autenticazione e ottiene risposta affermativa e il JWT nell'header (mock Service)
    @Test
    public void LoginSuccessful() throws UnsupportedEncodingException {
        //here I must create findUser
        when(loginService.findUser("alex@quicktutorialz.com", "Hello")).thenReturn("aaaa.bbbb.cccc");

        ResponseEntity<JsonResponseBody> actualHttpResponse = restController.login("alex@quicktutorialz.com", "Hello");

        assertEquals(HttpStatus.OK, actualHttpResponse.getStatusCode());
        assertEquals("Success! User logged in.",(String) actualHttpResponse.getBody().getResponse());
        assertEquals("aaaa.bbbb.cccc", actualHttpResponse.getHeaders().get("jwt").get(0).toString());

    }


    //l'utente invia email e password al controller per l'autenticazione e ottiene risposta negativa perche' non trovato perche' l'email e' sbagliata (mock Service)
    @Test
    public void LoginWithWronEmail() throws UnsupportedEncodingException {
        //here I must create findUser
        when(loginService.findUser("alex@quicktutorialz.com", "Hello")).thenReturn(null);

        ResponseEntity<JsonResponseBody> actualHttpResponse = restController.login("alex@quicktutorialz.com", "Hello");

        assertEquals(HttpStatus.FORBIDDEN, actualHttpResponse.getStatusCode());
        assertEquals("Wrong email or password.",(String) actualHttpResponse.getBody().getResponse());
        assertNull(actualHttpResponse.getHeaders().get("jwt"));

    }


    //l'utente invia email e password al controller per l'autenticazione e ottiene risposta negativa perche' non trovato perche' la pwd e' sbagliata (mock Service)
    @Test
    public void LoginWithWrongPwd() throws UnsupportedEncodingException {
        //here I must create findUser
        when(loginService.findUser("alex@quicktutorialz.com", "Hello")).thenReturn(null);

        ResponseEntity<JsonResponseBody> actualHttpResponse = restController.login("alex@quicktutorialz.com", "Hello");

        assertEquals(HttpStatus.FORBIDDEN, actualHttpResponse.getStatusCode());
        assertEquals("Wrong email or password.",(String) actualHttpResponse.getBody().getResponse());
        assertNull(actualHttpResponse.getHeaders().get("jwt"));

    }

    //il filtro fa il forward della request a /loginError perchè ha trovato errori di autenticazione
    @Test
    public void loginError(){

        ResponseEntity<JsonResponseBody> response = restController.errorMessage("Errore");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: Errore", response.getBody().getResponse());
    }


    //l'utente chiede di vedere i suoi ToDos e li ottiene (mock toDoService )
    @Test
    public void showToDos(){

        //mock what it's done by filter with the JWT
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", "alex@quicktutorialz.com");
        userData.put("name", "Alessandro Argentieri");
        userData.put("exp_date", new Date(13/12/2017));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userData")).thenReturn(userData);

        //mock what it's done by toDoService
        List<ToDo> expectedToDos = new ArrayList<>();
        expectedToDos.add(new ToDo(1, "Doing Things", new Date(12/12/2017), "low", "alex@quicktutorialz.com"));
        expectedToDos.add(new ToDo(2, "Make Stuff", new Date(12/12/2017), "high", "alex@quicktutorialz.com"));

        when(toDoService.findToDosByEmail("alex@quicktutorialz.com")).thenReturn(expectedToDos);

        ResponseEntity<JsonResponseBody> response = restController.showToDos(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedToDos, response.getBody().getResponse());
    }


    //l'utente chiede di vedere i suoi ToDos ma qualcosa va storto e ottiene il messaggio di errore
    @Test
    public void showToDosButSomethingGoesWrong(){

        //mock what it's done by filter with the JWT
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", "alex@quicktutorialz.com");
        userData.put("name", "Alessandro Argentieri");
        userData.put("exp_date", new Date(13/12/2017));

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getAttribute("userData")).thenReturn(userData);

        //mock what it's done by toDoService
        List<ToDo> expectedToDos = new ArrayList<>();
        expectedToDos.add(new ToDo(1, "Doing Things", new Date(12/12/2017), "low", "alex@quicktutorialz.com"));
        expectedToDos.add(new ToDo(2, "Make Stuff", new Date(12/12/2017), "high", "alex@quicktutorialz.com"));

        Exception e = new RuntimeException();
        when(request.getAttribute("userData")).thenThrow(e);

        ResponseEntity<JsonResponseBody> response = restController.showToDos(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: " + e.toString(), response.getBody().getResponse());
    }


    //l'utente chiede di salvare un ToDos e ci riesce (mock toDoService)
    @Test
    public void saveToDo(){

        //later inserted
        BindingResult bindingResult = mock(BindingResult.class);

        ToDo toDoToBeSaved = new ToDo(1, "Study Spring", new Date(13/12/2017), "high", "alex@quicktutorialz.com");

        when(toDoService.saveToDo(toDoToBeSaved)).thenReturn(toDoToBeSaved);

        ResponseEntity<JsonResponseBody> response = restController.newToDo(toDoToBeSaved, bindingResult);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(toDoToBeSaved, response.getBody().getResponse());
    }


    //l'utente chiede di salvare un ToDos e non ci riesce perche' non e' valido per JSR-303 (mock toDoService ) e da l'errore in output
    @Test
    public void saveToDoJSR303Invalid(){

        ToDo toDoToBeSaved = new ToDo(1, null, new Date(13/12/2017), "high", null);

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<JsonResponseBody> response = restController.newToDo(toDoToBeSaved, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getResponse() instanceof String);
    }

    //l'utente chiede di salvare un ToDos e non ci riesce perche' non e' valido per Spring Validator (mock toDoService )
    @Test
    public void saveToDoSpringValidatorInvalid(){

        ToDo toDoToBeSaved = new ToDo(1, "Test Code", new Date(13/12/2017), "middle", "alex@quicktutorialz.com");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);

        ResponseEntity<JsonResponseBody> response = restController.newToDo(toDoToBeSaved, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getResponse() instanceof String);
    }

    //l'utente chiede si salvare un ToDos ma qualcosa va storto nel salvataggio e viene lanciata un'Eccezione
    @Test
    public void saveToDoButException(){

        ToDo toDoToBeSaved = new ToDo(1, "Test Code", new Date(13/12/2017), "middle", "alex@quicktutorialz.com");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);

        Exception e = new RuntimeException();
        when(toDoService.saveToDo(toDoToBeSaved)).thenThrow(e);

        ResponseEntity<JsonResponseBody> response = restController.newToDo(toDoToBeSaved, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: " + e.toString(), response.getBody().getResponse());
    }

    //l'utente chiede di cancellare un ToDos(mock toDoService )
    @Test
    public void deleteToDo(){

        int id = 4;
        ResponseEntity<JsonResponseBody> response = restController.deleteToDo(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ToDo deleted.", response.getBody().getResponse());

    }

    //l'utente chiede di cancellare un ToDos(mock toDoService ) ma qualcosa va storto
    @Test
    public void deleteToDoButThrowsException(){

        int id = 4;
        Exception e = new RuntimeException();
        // when(toDoService.deleteToDo(id)).thenThrow(e); doesn't work
        doThrow(e).when(toDoService).deleteToDo(id);
        ResponseEntity<JsonResponseBody> response = restController.deleteToDo(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("ToDo deleted.", response.getBody().getResponse());

    }



}