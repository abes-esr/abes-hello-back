package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.entities.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test la couche DAO de l'entité AppUser avec la base de données.
 * Les tests ne concernent que l'insertion et la lecture.
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest // Permet d'utiliser une base de données H2 en mémoire pour les tests.
/**
 * Le module core ne contient aucun Boot d'application Spring car il n'est jamais exécuté seul.
 * Les annotations suivantes permettent de simuler un Boot de Spring. *
 */
@EnableAutoConfiguration
@EntityScan(basePackages = "fr.abes.helloabes.core.entities")
@ContextConfiguration(classes = AppUser.class)
public class IUserDaoTest {

    @Autowired
    private IUserDao userDao;

    protected static AppUser getAdminUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
        return user;
    }

    /**
     * Test l'ajout d'un utilisateur
     */
    @Test
    public void saveAdminUser() {
        AppUser myNewUser = userDao.save(getAdminUser());
        assertEquals("admin", myNewUser.getUserName());
        assertEquals("@totoTOTO1234", myNewUser.getPassWord());
    }

    /**
     * Test l'ajout d'un utilisateur dont le nom d'utilisateur existe dejà
     */
    @Test
    public void saveAlreadyExistingUserName() {

        userDao.save(getAdminUser());

        AppUser mySecondUser = getAdminUser();
        mySecondUser.setPassWord("1234TOTOtoto@");

        userDao.save(mySecondUser);

        Exception exception = assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            userDao.findByUserName("admin");
        });
    }

    /**
     * Test l'ajout d'un utilisateur dont le nom d'utilisateur est null
     */
    @Test
    public void saveNullUserName() {
        AppUser myUser = getAdminUser();
        myUser.setUserName(null);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test l'ajout d'un utilisateur dont le nom d'utilisateur est vide
     */
    @Test
    public void saveEmptyUserName() {
        AppUser myUser = getAdminUser();
        myUser.setUserName("");

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être vide";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test l'ajout d'un utilisateur dont le mot de passe est null
     */
    @Test
    public void saveNullPassword() {
        AppUser myUser = getAdminUser();
        myUser.setPassWord(null);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne doit pas être null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test l'ajout d'un utilisateur dont le mot de passe est faible
     */
    @Test
    public void saveWeakPassword() {

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            AppUser myUser = new AppUser("admin", "admin");
            userDao.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne respecte pas les règles de sécurité";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * Test la recherche d'un utilisateur existant par son nom d'utilisateur
     */
    @Test
    public void findExistUserByUserName() {
        userDao.save(getAdminUser());

        AppUser myCandidate = userDao.findByUserName("admin");
        assertEquals("admin", myCandidate.getUserName());
        assertEquals("@totoTOTO1234", myCandidate.getPassWord());
    }

    /**
     * Test la recherche d'un utilisateur qui n'existe pas par son nom d'utilisateur
     */
    @Test
    public void findNotExistsUserByUserName() {
        userDao.save(getAdminUser());

        AppUser myCandidate = userDao.findByUserName("corentin");
        assertNull(myCandidate);
    }
    
}
