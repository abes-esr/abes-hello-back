package fr.abes.helloabes.web;

import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.web.configuration.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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

    @MockBean
    protected IUserDao userRepository;

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
    protected static AppUser getAdminUser() {
        final AppUser user;
        user = new AppUser("admin", "@adminADMIN1234");
        return user;
    }

    /**
     * Retourne un utilisateur avec un nom d'utilisateur test
     * @return AppUser
     */
    protected static AppUser getTestUser() {
        final AppUser user;
        user = new AppUser("test", "@testTEST1234");
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

        myDataBaseUser.setIdentityNumber(1);
        myDataBaseUser.setPassWord(encoder().encode(myUser.getPassWord()));
        assertTrue(encoder().matches(myUser.getPassWord(), myDataBaseUser.getPassWord()));

        return myDataBaseUser;
    }
}
