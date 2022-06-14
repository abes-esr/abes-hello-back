package fr.abes.helloabes;

import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.dao.ISupplierDao;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.entities.Supplier;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;
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
@Slf4j
public class HelloABESApplication extends SpringBootServletInitializer implements CommandLineRunner {

	/** Dépot d'utilisateurs du service web. L'attribut {@link #userDao} est utilisé ici dans le cadre de
	 * la version de démontration afin d'ajouter dès le démarrage un utilisateur par defaut.
	 * La base de donnée du projet est une base de donnée volatile H2 qui nécessite de remplir la base à chaque démarrage.
	 * <p> Note : il est important de supprimer cet attribut dans une version de production.</p>
	 */
	@Autowired
	private IUserDao userDao;
	@Autowired
	private IOrderDao orderDao;
	@Autowired
	private ISupplierDao supplierDao;
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
	 * Configure le serveur
	 * @param builder
	 * @return
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HelloABESApplication.class);
	}

	/**
	 * Fonction exécutée au démarrage du service. Il s'agit ici d'une surcharge afin de modifier
	 * le comportement original du démarrage afin d'ajouter un utilisateur par défaut.
	 * <p> Note : il est important de supprimer cette fonction dans une version de production.</p>
	 * @param args Tableau des arguments passés à l'appel de la fonction. Par défaut, il ne contient rien.
	 * @throws Exception si l'utilisateur ne peut pas être crée ou s'il ne peut pas être ajouter à la collection des utilisateurs.
	 */
	@Override
	@Transactional
	public void run(String... args) throws Exception {

		log.debug("Ajout de commandes dans la base de données.");

		List<Supplier> listOfFournisseurs = supplierDao.findAll();
		List<AppUser> listOfUsersDemo = userDao.findAll();
		List<Product> products = productDao.findAll();

		List<Product> productDemo1 = Arrays.asList(
				products.stream().filter(p -> p.getName().contains("Livre Harry Potter"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Livre Energie vagabonde"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Livre Akira"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Le Monde"))
						.findAny().orElse(null)
		);

		List<Product> productDemo2 = Arrays.asList(
				products.stream().filter(p -> p.getName().contains("La Souris sans fil"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Iphone 12"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Iphone 12"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Samsung S20"))
						.findAny().orElse(null)
		);

		List<Product> productDemo3 = Arrays.asList(
				products.stream().filter(p -> p.getName().contains("Tablette Samsung"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Iphone 12"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("L'écran HP"))
						.findAny().orElse(null),
				products.stream().filter(p -> p.getName().contains("Samsung S20"))
						.findAny().orElse(null)
		);

		List<Order> commandes = Arrays.asList(
				new Order(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("admin"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Abes"))
								.findAny().orElse(null),
						productDemo1
						),
				new Order(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("admin"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Boulanger"))
								.findAny().orElse(null),
						productDemo2
				),
				new Order(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("demoUser1"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Darty"))
								.findAny().orElse(null),
						productDemo3
				),
				new Order(
						listOfUsersDemo.stream().filter(u -> u.getUserName().contains("toto"))
								.findAny().orElse(null),
						listOfFournisseurs.stream().filter(f -> f.getName().contains("Darty"))
								.findAny().orElse(null),
						productDemo3
				)
		);

		orderDao.saveAll(commandes);

		//userDao.delete(userDao.findByUserName("demoUser1"));

		log.trace("Trace Message!");
		log.debug("Debug Message!");
		log.info("Info Message!");
		log.warn("Warn Message!");
		log.error("Error Message!");
	}

}
