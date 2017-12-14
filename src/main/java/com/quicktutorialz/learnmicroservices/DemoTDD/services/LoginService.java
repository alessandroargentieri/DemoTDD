package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import java.io.UnsupportedEncodingException;

public interface LoginService {

    String findUser(String email, String pwd) throws UnsupportedEncodingException;
}
