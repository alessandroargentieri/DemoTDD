package com.quicktutorialz.learnmicroservices.DemoTDD;

import com.quicktutorialz.learnmicroservices.DemoTDD.daos.ToDoDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.daos.UserDao;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.ToDo;
import com.quicktutorialz.learnmicroservices.DemoTDD.entities.User;
import com.quicktutorialz.learnmicroservices.DemoTDD.utilities.EncryptionUtils;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@Log
@ServletComponentScan               //fa funzionare il filtro
@SpringBootApplication
public class DemoTddApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoTddApplication.class, args);
	}

	@Autowired
	UserDao userDao;

	@Autowired
	ToDoDao toDoDao;

	@Autowired
	EncryptionUtils encryptionUtils;

	@Value("${server.ip}")
	String serverIp;

	@Value("${karate.contact}")
	String cintura;

	@Bean
	CommandLineRunner commandLineRunner(){
		return new CommandLineRunner() {
			@Override
			public void run(String... strings) throws Exception {
				log.info("Server ip is: " + serverIp);
				log.info("La tua cintura è: " + cintura);

				String encryptedPwd;
				encryptedPwd = encryptionUtils.encrypt("Hello");
				userDao.save(new User("alex@quicktutorialz.com", "Alessandro Argentieri", encryptedPwd));

				encryptedPwd = encryptionUtils.encrypt("MyPwd");
				userDao.save(new User("franz@quicktutorialz.com", "Franz Leroy", encryptedPwd));

				encryptedPwd = encryptionUtils.encrypt("Belle");
				userDao.save(new User("annabelle@quicktutorialz.com", "Annabelle Sorah", encryptedPwd));


				toDoDao.save( new ToDo(1, "Learn Microservices", new Date(), "high", "alex@quicktutorialz.com"));
				toDoDao.save( new ToDo(null, "Learn Spring boot", null, "low", "alex@quicktutorialz.com"));

				toDoDao.save( new ToDo(3, "Feed Animals", new Date(), "high", "franz@quicktutorialz.com"));
				toDoDao.save( new ToDo(null, "Go to take Jim", null, "low", "franz@quicktutorialz.com"));

				toDoDao.save( new ToDo(5, "By a new Car", new Date(), "high", "annabelle@quicktutorialz.com"));
				toDoDao.save( new ToDo(null, "Go to the Gim", null, "low", "annabelle@quicktutorialz.com"));

			}



		};
	}
}
