package fr.abes.helloabes;

import fr.abes.helloabes.core.dao.ICommandeDao;
import fr.abes.helloabes.core.dao.IFournisseurDao;
import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Commandes;
import fr.abes.helloabes.core.entities.Fournisseur;
import fr.abes.helloabes.core.entities.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

/**
 * La classe {@code CoreApplication} représente la classe principale du service web RESTFul.
 * Cette classe est basée sur le framework Spring et contient la fonction principale {@link #main(String[])} appelée au
 * démarrage du framework.
 * @since 0.0.1
 * @author Duy Tran
 */
@SpringBootApplication
public class HelloABESApplication implements CommandLineRunner {

	/** Dépot d'utilisateurs du service web. L'attribut {@link #userRepository} est utilisé ici dans le cadre de
	 * la version de démontration afin d'ajouter dès le démarrage un utilisateur par defaut.
	 * La base de donnée du projet est une base de donnée volatile H2 qui nécessite de remplir la base à chaque démarrage.
	 * <p> Note : il est important de supprimer cet attribut dans une version de production.</p>
	 */
	@Autowired
	private IUserDao userRepository;
	@Autowired
	private ICommandeDao commandeDao;
	@Autowired
	private IFournisseurDao fournisseurDao;
	@Autowired
	private IProductDao productDao;

	/**
	 * Fonction principale exécutée à l'initialisation du framework Spring.
	 * @param args Tableau des arguments passées à l'appel de la fonction.  Par défaut, il ne contient rien.
	 */
	public static void main(String[] args) {
		SpringApplication.run(HelloABESApplication.class, args);
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

		List<AppUser> listOfUsersDemo = Arrays.asList(
				new AppUser("admin", "$2a$10$gDbTV0zgAmKX350ggJ7W7.zYUWR8H/KWzW9.yrl9z80uuzZ73kppy"),
				new AppUser("demoUser1", "$2a$10$gDbTV0zgAmKX350ggJ7W7.zYUWR8H/KWzW9.yrl9z80uuzZ73kppy"),
				new AppUser("demoUser2", "$2a$10$gDbTV0zgAmKX350ggJ7W7.zYUWR8H/KWzW9.yrl9z80uuzZ73kppy"),
				new AppUser("demoUser3", "$2a$10$gDbTV0zgAmKX350ggJ7W7.zYUWR8H/KWzW9.yrl9z80uuzZ73kppy"),
				new AppUser("demoUser3", "$2a$10$gDbTV0zgAmKX350ggJ7W7.zYUWR8H/KWzW9.yrl9z80uuzZ73kppy")
		);

		userRepository.saveAll(listOfUsersDemo);

		List<Fournisseur> listOfFournisseurs = Arrays.asList(
				new Fournisseur("Boulanger"),
				new Fournisseur("Darty"),
				new Fournisseur("Abes")
		);

		fournisseurDao.saveAll(listOfFournisseurs);

		List<Products> products = Arrays.asList(
				new Products("Livre Harry Potter", 17.80),
				new Products("Livre Energie vagabonde", 28.40),
				new Products("Livre Akira", 14.50),
				new Products("Le Monde", 4.50),
				new Products("Le Souris sans fil", 12.60),
				new Products("L'écran HP", 350.00),
				new Products("Tablette Samsung", 480.70),
				new Products("Samsung S20", 890.40),
				new Products("Iphone 12", 1090.50)
		);

		productDao.saveAll(products);


		List<Products> productDemo1 = Arrays.asList(
				new Products("Livre Harry Potter", 17.80),
				new Products("Livre Energie vagabonde", 28.40),
				new Products("Livre Akira", 14.50),
				new Products("Le Monde", 4.50)
		);

		List<Products> productDemo2 = Arrays.asList(
				new Products("Le Souris sans fil", 12.60),
				new Products("L'écran HP", 350.00),
				new Products("Tablette Samsung", 480.70)
		);

		List<Products> productDemo3 = Arrays.asList(
				new Products("Samsung S20", 890.40),
				new Products("Iphone 12", 1090.50)
		);

		List<Products> productDemo4 = Arrays.asList(
				new Products("Livre Energie vagabonde", 28.40),
				new Products("Iphone 12", 1090.50),
				new Products("Le Monde", 4.50)
		);

		fournisseurDao.saveAll(listOfFournisseurs);

		List<Commandes> commandes = Arrays.asList(
				new Commandes(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("admin"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Abes"))
								.findAny().orElse(null),
						productDemo1
						),
				new Commandes(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("demoUser1"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Boulanger"))
								.findAny().orElse(null),
						productDemo2
				),
				new Commandes(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("demoUser2"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Darty"))
								.findAny().orElse(null),
						productDemo3
				),
				new Commandes(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("demoUser3"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Darty"))
								.findAny().orElse(null),
						productDemo4
				)
		);

		commandeDao.saveAll(commandes);

	}

}
