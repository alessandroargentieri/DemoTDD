package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import com.quicktutorialz.learnmicroservices.DemoTDD.DemoTddApplication;
import com.quicktutorialz.learnmicroservices.DemoTDD.daos.UserDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.User;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.EncryptionUtils;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JwtUtils;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.ReflectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = DemoTddApplication.class)  //load configuration file
@RunWith(SpringRunner.class)
public class LoginServiceImplTest {

    @Autowired @InjectMocks
    LoginService loginService;

    @Mock
    UserDao userDao;

    @Mock JwtUtils jwtUtils;

    @Mock EncryptionUtils encryptionUtils;

    //test che il loginService contenga un'istanza di JwtUtils, di ToDoDao e UserDao e di EncryptionUtils.
    @Test
    public void ContainsUserDaoAndUtils() throws Exception{
        assertNotNull(ReflectionUtils.getPrivateField(loginService, "userDao"));
        assertTrue(ReflectionUtils.getPrivateField(loginService, "userDao") instanceof UserDao);

        assertNotNull(ReflectionUtils.getPrivateField(loginService, "jwtUtils"));
        assertTrue(ReflectionUtils.getPrivateField(loginService, "jwtUtils") instanceof JwtUtils);

        assertNotNull(ReflectionUtils.getPrivateField(loginService, "encryptionUtils"));
        assertTrue(ReflectionUtils.getPrivateField(loginService, "encryptionUtils") instanceof EncryptionUtils);

    }


    //////////////////


    //il service riceve username e password ma l'utente non esiste, quindi restituisce un jwt nullo (mock Jwt e Dao)
    @Test
    public void userNotExist() throws UnsupportedEncodingException {
        when(userDao.findByEmail("alex@quicktutorialz.com")).thenReturn(Optional.empty());
        assertEquals(null, loginService.findUser("alex@quicktutorialz.com", "Hello"));
    }

    //il service riceve username e password e l'utente esiste ma la password e' sbagliata, quindi restituisce un jwt nullo
    @Test
    public void passwordWrong() throws UnsupportedEncodingException {
        when(userDao.findByEmail("alex@quicktutorialz.com")).thenReturn(Optional.empty());
        assertEquals(null, loginService.findUser("alex@quicktutorialz.com", "Hello"));
    }

    //il service riceve username e password e l'utente esiste, quindi restituisce il jwt
    @Test
    public void loginSuccess() throws UnsupportedEncodingException {
        User user = new User("alex@quicktutorialz.com", "Alessandro Argentieri", "HelloEncrypted");
        Optional<User> userr = Optional.of(user);
        when(userDao.findByEmail("alex@quicktutorialz.com")).thenReturn(userr);
        when(   encryptionUtils.decrypt( user.getPassword() )).thenReturn("Hello");

        when(jwtUtils.generateJwt(any(String.class), any(String.class),any(Date.class))).thenReturn("aaaa.bbbb.cccc");

        assertEquals("aaaa.bbbb.cccc", loginService.findUser("alex@quicktutorialz.com", "Hello"));
    }




}