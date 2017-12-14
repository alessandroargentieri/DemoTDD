package com.quicktutorialz.learnmicroservices.DemoTDD.services;

import com.quicktutorialz.learnmicroservices.DemoTDD.daos.UserDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.User;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.EncryptionUtils;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.JwtUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

@Log
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    UserDao userDao;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EncryptionUtils encryptionUtils;

    @Override
    public String findUser(String email, String pwd) throws UnsupportedEncodingException{
        Optional<User> userr = userDao.findByEmail(email);
        if(userr.isPresent()){
            User user = userr.get();
            if(encryptionUtils.decrypt(user.getPassword()).equals(pwd)) {
                Date expDate = new Date();
                expDate.setTime(expDate.getTime() + 300 * 1000);
                return jwtUtils.generateJwt(user.getEmail(), user.getName(), expDate);
            }
        }
         return null;
    }


}
