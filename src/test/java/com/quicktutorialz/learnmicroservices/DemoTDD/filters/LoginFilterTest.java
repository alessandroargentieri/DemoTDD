package com.quicktutorialz.learnmicroservices.DemoTDD.filters;

import com.quicktutorialz.learnmicroservices.DemoTDD.DemoTddApplication;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JwtUtils;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.ReflectionUtils;

import io.jsonwebtoken.ExpiredJwtException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockRequestDispatcher;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DemoTddApplication.class)  //load configuration file
@RunWith(SpringRunner.class)
public class LoginFilterTest {  //1 classe creata

    //test che il loginFilter contenga un'istanza di JwtUtils
    @Autowired @InjectMocks
    LoginFilter loginFilter;

    @Mock
    JwtUtils jwtUtils;

    @Test
    public void ContainsJwtUtils() throws Exception{
        assertNotNull(ReflectionUtils.getPrivateField(loginFilter, "jwtUtils"));
        assertTrue(ReflectionUtils.getPrivateField(loginFilter, "jwtUtils") instanceof JwtUtils);
    }

/*
    //l'utente fa una richiesta di login ed essa passa per il filterChain
    @Test
    public void SigninPassesThroughChain() throws IOException, ServletException {
        HttpServletRequest  request     = mock(HttpServletRequest.class);
        HttpServletResponse response    = mock(HttpServletResponse.class);
        FilterChain         filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("http://localhost:8684/signin.html");
        when(request.getServletPath()).thenReturn("/signin.html");

        loginFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }
    //l'utente fa una richiesta che non sia il login e senza il jwt nell'header della request e c'e' il redirect
    @Test
    public void NoSigninNoJwtThenRedirect() throws IOException, ServletException {
        HttpServletRequest  request     = mock(HttpServletRequest.class);
        HttpServletResponse response    = mock(HttpServletResponse.class);
        FilterChain         filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("http://localhost:8684/showToDos");
        when(request.getServletPath()).thenReturn("/showToDos");
        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn(null);

        loginFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("http://localhost:8684/signin.html");
    }
    //l'utente fa una richiesta che non sia il login e con il jwt nell'header della request e passa per il filterChain.
    @Test
    public void NoSigninWithJwtThenChain() throws IOException, ServletException {
        HttpServletRequest  request     = mock(HttpServletRequest.class);
        HttpServletResponse response    = mock(HttpServletResponse.class);
        FilterChain         filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("http://localhost:8684/showToDos");
        when(request.getServletPath()).thenReturn("/showToDos");
        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn("aaaa.bbbb.cccc");

        Map<String, Object> userData = new HashMap<String, Object>();
        userData.put("email", "alex@quicktutorialz.com");
        userData.put("name", "Alessandro Argentieri");
        userData.put("exp_date", new Date(12/12/2017));

        when(jwtUtils.jwt2Map("aaaa.bbbb.cccc")).thenReturn(userData);

        loginFilter.doFilter(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
    }

    //l'utente fa una richiesta che non sia il login con un jwt scaduto nell'header della request e c'e' il redirect
    @Test
    public void NoSigninWithExpiredJwtThenRedirect() throws IOException, ServletException {
        HttpServletRequest  request     = mock(HttpServletRequest.class);
        HttpServletResponse response    = mock(HttpServletResponse.class);
        FilterChain         filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("http://localhost:8684/showToDos");
        when(request.getServletPath()).thenReturn("/showToDos");
        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn("aaaa.bbbb.cccc");

        when(jwtUtils.jwt2Map("aaaa.bbbb.cccc")).thenThrow(new ExpiredJwtException(null, null, null));


        loginFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("http://localhost:8684/signin.html?message=Session%Expired");
    }

    //l'utente fa una richiesta che non sia il login con un jwt corrotto nell'header della request e c'e' il redirect
    @Test
    public void NoSigninWithCorruptedJwtThenRedirect() throws IOException, ServletException {
        HttpServletRequest  request     = mock(HttpServletRequest.class);
        HttpServletResponse response    = mock(HttpServletResponse.class);
        FilterChain         filterChain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("http://localhost:8684/showToDos");
        when(request.getServletPath()).thenReturn("/showToDos");
        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn("aaaa.bbbb.cccc");

        when(jwtUtils.jwt2Map("aaaa.bbbb.cccc")).thenThrow(new UnsupportedEncodingException());
        loginFilter.doFilter(request, response, filterChain);

        verify(response).sendRedirect("http://localhost:8684/signin.html?message=Token%Corrupted");
    }*/


/**
       - /signin.html, /login, /loginError   -> richiesta passa direttamente
       -------------------------------------------------------
       - per tutte le altre richieste si analizza il jwt:
             a. jwt nullo, scaduto, corrotto  -> /loginError
             b. jwt valido                    -> dati recuperati dal jwt e salvati nella richiesta e passa


 */

    HttpServletRequest  request;
    HttpServletResponse response;
    FilterChain         chain;

    @Before
    public void setUp(){
        request     = mock(HttpServletRequest.class);
        response    = mock(HttpServletResponse.class);
        chain       = mock(FilterChain.class);
    }


    //l'utente chiede di accedere a signin.html (risorsa statica) e la richiesta passa
    @Test
    public void notControlledAccesseForSigninPage() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn("/signin.html");

        loginFilter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);

    }

    //l'utente chiede di accedere a signin.html (risorsa statica) o al path /login o /loginError e la richiesta passa
    @Test
    public void notControlledAccesseForLogin() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn("/login");

        loginFilter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);

    }

    //l'utente chiede di accedere a signin.html (risorsa statica) o al path /login o /loginError e la richiesta passa
    @Test
    public void notControlledAccessForLoginError() throws IOException, ServletException {
        when(request.getServletPath()).thenReturn("/loginError");

        loginFilter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);

    }




    //l'utente chiede altro e viene analizzato il jwt che è null, quindi inoltra la stessa request a /loginError
    @Test
    public void NotAllowedAccessWithNullJwt() throws IOException, ServletException {
        HttpServletRequest  request = mock(MockHttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();

        when(request.getServletPath()).thenReturn("/showToDos");
        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn(null);
        RequestDispatcher dispatcher = new MockRequestDispatcher("/loginError?error=You must login first!");
        when(request.getRequestDispatcher("/loginError?error=You must login first!")).thenReturn(dispatcher);

        loginFilter.doFilter(request, response, chain);
        verify(request).getRequestDispatcher("/loginError?error=You must login first!");
    }

    //l'utente chiede altro e viene analizzato il jwt che è invalido: inoltra la stessa request a /loginError
    @Test
    public void NotAllowedAccessWithInvalidJwt() throws IOException, ServletException {
        HttpServletRequest  request = mock(MockHttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();

        when(request.getServletPath()).thenReturn("/showToDos");

        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn("aaaa.bbbb.cccc");
       // doThrow(new Exception()).when(jwtUtils).jwt2Map("aaaa.bbbb.cccc");
        when(jwtUtils.jwt2Map("aaaa.bbbb.cccc")).thenThrow(new RuntimeException());

        RequestDispatcher dispatcher = new MockRequestDispatcher("/loginError?error=You must login first!");
        when(request.getRequestDispatcher(any(String.class))).thenReturn(dispatcher);

        loginFilter.doFilter(request, response, chain);
        verify(request).getRequestDispatcher(any(String.class));
    }

    //l'utente chiede altro e viene analizzato il jwt che è valido: salva i dati recuperati nella request e la richiesta passa
    @Test
    public void allowedAccesses() throws IOException, ServletException {
        HttpServletRequest  request = mock(MockHttpServletRequest.class);
        HttpServletResponse response = new MockHttpServletResponse();

        Map<String, Object> userData = new HashMap();
        userData.put("email", "alex@quicktutorialz.com");
        userData.put("name", "Alessandro Argentieri");
        userData.put("password", "Hello");

        when(request.getServletPath()).thenReturn("/showToDos");

        when(jwtUtils.getJwtFromHttpRequest(request)).thenReturn("aaaa.bbbb.cccc");
        when(jwtUtils.jwt2Map("aaaa.bbbb.cccc")).thenReturn(userData);

        loginFilter.doFilter(request, response, chain);
        verify(chain).doFilter(request, response);
    }

}