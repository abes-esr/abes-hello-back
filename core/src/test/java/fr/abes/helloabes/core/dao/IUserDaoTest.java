package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.AppUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
    private IUserDao userRepository;

    protected static AppUser getRandomAppUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
        return user;
    }

    // Tests d'écriture
    @Test
    public void testSaveRandomAppUser() {
        AppUser myNewUser = userRepository.save(getRandomAppUser());
        assertEquals("admin", myNewUser.getUserName());
        assertEquals("@totoTOTO1234", myNewUser.getPassWord());
    }

    @Test
    public void testSaveAlreadyExistingUserName() {

        userRepository.save(getRandomAppUser());

        AppUser mySecondUser = getRandomAppUser();
        mySecondUser.setPassWord("1234TOTOtoto@");

        userRepository.save(mySecondUser);

        Exception exception = assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            userRepository.findByUserName("admin");
        });
    }

    @Test
    public void testSaveNullUserName() {
        AppUser myUser = getRandomAppUser();
        myUser.setUserName(null);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSaveEmptyUserName() {
        AppUser myUser = getRandomAppUser();
        myUser.setUserName("");

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être vide";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSaveNullPassword() {
        AppUser myUser = getRandomAppUser();
        myUser.setPassWord(null);

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            userRepository.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne doit pas être null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testSavePasswordSecurity() {

        Exception exception = assertThrows(ConstraintViolationException.class, () -> {
            AppUser myUser = new AppUser("admin", "admin");
            userRepository.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne respecte pas les règles de sécurité";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    // Test de lecture
    @Test
    public void testFindByUserNameExistUser() {
        userRepository.save(getRandomAppUser());

        AppUser myCandidate = userRepository.findByUserName("admin");
        assertEquals("admin", myCandidate.getUserName());
        assertEquals("@totoTOTO1234", myCandidate.getPassWord());
    }

    @Test
    public void testFindByUserNameNotExistUser() {
        userRepository.save(getRandomAppUser());

        AppUser myCandidate = userRepository.findByUserName("corentin");
        assertNull(myCandidate);
    }

}
