package com.quicktutorialz.learnmicroservices.DemoTDD.filters;

import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

@Log
@Component
@WebFilter("/AuthenticationFilter")
public class LoginFilter implements Filter {

    @Autowired
    JwtUtils jwtUtils;


   @Override
   public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

       HttpServletRequest request = (HttpServletRequest) req;
       HttpServletResponse response = (HttpServletResponse) resp;

       if(Arrays.asList("/signin.html","/login", "/loginError").contains(request.getServletPath())){
           chain.doFilter(request, response);
       }else{
           String jwt= jwtUtils.getJwtFromHttpRequest(request);
           if(jwt == null){
               request.getRequestDispatcher("/loginError?error=You must login first!").forward(request, response);
           }else{
               try {
                   Map<String, Object> userData = jwtUtils.jwt2Map(jwt);
                   request.setAttribute("userData", userData);
                   chain.doFilter(request, response);
               }catch(ExpiredJwtException e){
                   request.getRequestDispatcher("/loginError").forward(request, response);
               }catch(UnsupportedEncodingException e2){
                   request.getRequestDispatcher("/loginError").forward(request, response);
               }catch(Exception e3){
                   request.getRequestDispatcher("/loginError?error=" + e3.toString()).forward(request, response);
               }
           }
       }
   }


    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }

    @Override
    public void destroy() {
    }


}