package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.AppUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import lombok.extern.slf4j.Slf4j;


/**
 * Test la couche DAO de l'entité AppUser avec la base de données.
 * Les tests ne concernent que l'insertion et la lecture.
 * Le module core ne contient aucun Boot d'application Spring car il n'est jamais exécuté seul.
 * Les annotations suivantes permettent de simuler un Boot de Spring. *
 * @since 0.0.1
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest // Permet d'utiliser une base de données H2 en mémoire pour les tests.
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
        Assertions.assertEquals("admin", myNewUser.getUserName());
        Assertions.assertEquals("@totoTOTO1234", myNewUser.getPassWord());

        log.info("Test réussi. Réussi : {}", userDao.findAll());
    }

    /**
     * Teste la récupération d'un utilisateur dont le nom d'utilisateur existe deux fois en base de données
     */
    @Test
    public void findByUserNameWithTwoResult() {

        userDao.save(getAdminUser());

        AppUser mySecondUser = getAdminUser();
        mySecondUser.setPassWord("1234TOTOtoto@");

        userDao.save(mySecondUser);

        IncorrectResultSizeDataAccessException result = Assertions.assertThrows(IncorrectResultSizeDataAccessException.class, () -> {
            userDao.findByUserName("admin");
        });

        log.info("Test réussi. {}", result.getMessage());
    }

    /**
     * Teste l'ajout d'un utilisateur dont le nom d'utilisateur est null
     */
    @Test
    public void saveNullUserName() {
        AppUser myUser = getAdminUser();
        myUser.setUserName(null);

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être null";
        String actualMessage = constraintViolationException.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        log.info("Test réussi. {}", constraintViolationException.getMessage());
    }

    /**
     * Teste l'ajout d'un utilisateur dont le nom d'utilisateur est vide
     */
    @Test
    public void saveEmptyUserName() {
        AppUser myUser = getAdminUser();
        myUser.setUserName("");

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le nom d'utilisateur ne doit pas être vide";
        String actualMessage = constraintViolationException.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        log.info("Test réussi. {}", constraintViolationException.getMessage());
    }

    /**
     * Teste l'ajout d'un utilisateur dont le mot de passe est null
     */
    @Test
    public void saveNullPassword() {
        AppUser myUser = getAdminUser();
        myUser.setPassWord(null);

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            userDao.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne doit pas être null";
        String actualMessage = constraintViolationException.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        log.info("Test réussi. {}", constraintViolationException.getMessage());
    }

    /**
     * Teste l'ajout d'un utilisateur dont le mot de passe est faible
     */
    @Test
    public void saveWeakPassword() {

        ConstraintViolationException constraintViolationException = Assertions.assertThrows(ConstraintViolationException.class, () -> {
            AppUser myUser = new AppUser("admin", "admin");
            userDao.save(myUser);
        });

        String expectedMessage = "Le mot de passe ne respecte pas les règles de sécurité";
        String actualMessage = constraintViolationException.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));

        log.info("Test réussi. {}", constraintViolationException.getMessage());
    }

    /**
     * Teste la recherche d'un utilisateur existant par son nom d'utilisateur
     */
    @Test
    public void findExistUserByUserName() {
        userDao.save(getAdminUser());

        AppUser myCandidate = userDao.findByUserName("admin");
        Assertions.assertEquals("admin", myCandidate.getUserName());
        Assertions.assertEquals("@totoTOTO1234", myCandidate.getPassWord());

        log.info("Test réussi. Résultat : {}", userDao.findAll());
    }

    /**
     * Teste la recherche d'un utilisateur qui n'existe pas par son nom d'utilisateur
     */
    @Test
    public void findNotExistsUserByUserName() {
        userDao.save(getAdminUser());

        AppUser myCandidate = userDao.findByUserName("corentin");
        Assertions.assertNull(myCandidate);

        log.info("Test réussi. Résultat : {}", userDao.findByUserName("corentin"));
    }
    
}
