package fr.abes.helloabes.core;

import fr.abes.helloabes.core.models.Users;
import fr.abes.helloabes.core.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class CoreApplication {

	@Autowired
	private UserServiceImpl userService;

	@PostConstruct
	public Users initUser(){
		Users user = new Users("admin", "admin");
		return userService.createUser(user);
	}
	public static void main(String[] args) {

		SpringApplication.run(CoreApplication.class, args);

	}

}
