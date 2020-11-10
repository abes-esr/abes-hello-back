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

/**
 * La classe {@code CoreApplication} représente la classe principale du service web RESTFul.
 * Cette classe est basée sur le framework Spring et contient la fonction principale {@link #main(String[])} appelée au
 * démarrage du framework.
 * @since 0.0.1
 * @author Duy Tran
 */
@SpringBootApplication
public class CoreApplication implements CommandLineRunner {

	/** Dépot d'utilisateurs du service web. L'attribut {@link #userRepository} est utilisé ici dans le cadre de
	 * la version de démontration afin d'ajouter dès le démarrage un utilisateur par defaut.
	 * La base de donnée du projet est une base de donnée volatile H2 qui nécessite de remplir la base à chaque démarrage.
	 * <p> Note : il est important de supprimer cet attribut dans une version de production.</p>
	 */
	@Autowired
	private IUserRepository userRepository;

	/**
	 * Fonction principale exécutée à l'initialisation du framework Spring.
	 * @param args Tableau des arguments passées à l'appel de la fonction.  Par défaut, il ne contient rien.
	 */
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	/**
	 * Fonction exécutée au démarrage du service. Il s'agit ici d'une surcharge afin de modifier
	 * le comportement original du démarrage afin d'ajouter un utilisateur par défaut.
	 * <p> Note : il est important de supprimer cette fonction dans une version de production.</p>
	 * @param args Tableau des arguments passés à l'appel de la fonction. Par défaut, il ne contient rien.
	 * @throws Exception si l'utilisateur ne peut pas être crée ou s'il ne peut pas être ajouter à la collection des utilisateurs.
	 */
	@Override
	public void run(String... args) throws Exception {
		AppUser user = new AppUser("admin", "$2a$10$u1Ir8gKNjr18.ZoScWz39.Q/XuF7atm/NolVhbXsQVV3o3w8ywjqG");
		userRepository.save(user);
	}
}
