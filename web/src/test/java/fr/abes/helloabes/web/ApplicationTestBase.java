package fr.abes.helloabes.web;

import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.entities.Supplier;
import fr.abes.helloabes.web.configuration.DtoMapperUtility;
import fr.abes.helloabes.web.configuration.JwtUtility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe de test général pour l'application.
 * Le dépôt DAO est emulé grâce au Mock.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = {HelloABESApplication.class})
public class ApplicationTestBase {

    protected BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected IOrderDao orderDao;

    @Autowired
    protected DtoMapperUtility dtoMapper;

    @Autowired
    protected JwtUtility jwtUtility;

    @Autowired
    protected AuthenticationManager authenticationManager;

    @Autowired
    protected MockMvc mockMvc;

    /**
     * Retourne un utilisateur avec un nom d'utilisateur admin
     * @return AppUser
     */
    protected static AppUser getTotoUser() {
        final AppUser user;
        user = new AppUser("toto", "@Totototo85");
        return user;
    }

    /**
     * Retourne l'utilisateur dans l'état stocké en base de données.
     * Dans la base de données, son identifiant vaut 1 et son mot de passe est crypté.
     * On vérifie que le mot de passe crypté correspond au mot de passe clair.
     * @param myUser AppUser Utilisateur à retourner dans l'état stocké en base de données
     * @return AppUser L'utilisateur dans l'état stocké en base de données
     */
    protected AppUser getDataBaseUser(AppUser myUser) {
        final AppUser myDataBaseUser = new AppUser(myUser.getUserName(),myUser.getPassWord());

        myDataBaseUser.setIdentityNumber(5);
        myDataBaseUser.setPassWord(encoder().encode(myUser.getPassWord()));
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(), myDataBaseUser.getPassWord()));

        return myDataBaseUser;
    }

    protected List<Order> getListOfOrders(AppUser user) {
        // Create supplier
        Supplier supplier = new Supplier();
        supplier.setId(2L);
        supplier.setName("Darty");

        // Create products
        Product product1 = new Product();
        product1.setId(6L);
        product1.setName("L'écran HP");
        product1.setPrice(350.0);

        // Create order
        Order order = new Order();
        order.setId(3L);
        order.setSupplier(supplier);
        order.setProducts(Collections.singletonList(product1));

        // Create listOrder
        List<Order> listOrder = new ArrayList<>();
        listOrder.add(order);

//        return orderDao.findOrdersOfUser(user); // TODO voir pourquoi la base de test est vide
        return listOrder;
    }
}
