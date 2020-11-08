package fr.abes.helloabes.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.helloabes.core.config.JwtUtil;
import fr.abes.helloabes.core.models.AppUser;
import fr.abes.helloabes.core.repository.IUserRepository;
import fr.abes.helloabes.core.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

	// !!! Seulement pour la démontration , à supprimer en production !!!
	@Autowired
	private IUserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	// !!! Seulement pour la démontration , à supprimer en production !!!
	@Override
	public void run(String... args) throws Exception {
		AppUser user = new AppUser("admin", "$2a$10$u1Ir8gKNjr18.ZoScWz39.Q/XuF7atm/NolVhbXsQVV3o3w8ywjqG");
		userRepository.save(user);
	}
}
