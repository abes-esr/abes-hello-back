package fr.abes.helloabes.web;

import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.dao.ISupplierDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.web.configuration.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertTrue;

/**
 * Classe de test général pour l'application.
 * Le dépôt DAO est emulé grâce au Mock.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = HelloABESApplication.class)
public class ApplicationTestBase {

    protected BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    protected IOrderDao orderDao;

    @Autowired
    protected ISupplierDao supplierDao;

    @Autowired
    protected IProductDao productDao;

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
     * On vérifie que le mot de passe crypté corresond au mot de passe clair.
     * @param myUser AppUser Utilisateur à retourner dans l'état stocké en base de données
     * @return AppUser L'utilisateur dans l'état stocké en base de données
     */
    protected AppUser getDataBaseUser(AppUser myUser) {
        final AppUser myDataBaseUser = new AppUser(myUser.getUserName(),myUser.getPassWord());

        myDataBaseUser.setIdentityNumber(5);
        myDataBaseUser.setPassWord(encoder().encode(myUser.getPassWord()));
        assertTrue(encoder().matches(myUser.getPassWord(), myDataBaseUser.getPassWord()));

        return myDataBaseUser;
    }
}
