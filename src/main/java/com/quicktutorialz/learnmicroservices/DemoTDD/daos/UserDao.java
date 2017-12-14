package com.quicktutorialz.learnmicroservices.DemoTDD.daos;

import com.quicktutorialz.learnmicroservices.DemoTDD.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
