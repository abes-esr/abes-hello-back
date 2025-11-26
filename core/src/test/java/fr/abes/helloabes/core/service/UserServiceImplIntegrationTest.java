package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.exception.UserAlreadyExistsException;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import lombok.extern.slf4j.Slf4j;

/**
 * Test d'intégration de la couche service des utilisateurs de l'application.
 * La base de données est remplacée par un Mock.
 */
@ExtendWith(SpringExtension.class)
@Slf4j
public class UserServiceImplIntegrationTest {

    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @MockitoBean
    IUserDao userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void setup(){
        userService = new UserServiceImpl(userRepository,encoder());
    }

    protected static AppUser getAdminUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
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
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(), myDataBaseUser.getPassWord()));

        return myDataBaseUser;
    }

    /**
     * Teste la recherche d'un utilisateur par son nom d'utilisateur
     */
    @Test
    public void findUserByUserName() {

        AppUser myCandidate = getAdminUser();

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myCandidate);

        AppUser myUser = userService.findUser(myCandidate);

        Assertions.assertEquals("admin", myUser.getUserName());

        log.info("Test réussi. UserName = {}", myUser.getUserName());
    }

    /**
     * Teste la création d'un nouvel utilisateur.
     * Teste le cryptage du mot de passe.
     */
    @Test
    public void createUser() {

        AppUser myUser = getAdminUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        // On mocke la DAO avec l'utilisateur à tester et l'utilisateur de la base de données
        Mockito.when(userRepository.save(myTestingUser)).thenReturn(myDataBaseUser);

        AppUser myCandidate = userService.createUser(myTestingUser);

        // Teste si le mot de passe utilisateur passé en paramètre a bien été crypté et correspond au mot de passe clair.
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(),myTestingUser.getPassWord()));
        log.info("Test réussi. L'encodage du mot de passe et la vérification de sa correspondance avec le mot de passe en clair a fonctionné correctement.");

        // Teste si le mapping DAO a bien fonctionné. Les noms d'utilisateur doivent correspondre.
        Assertions.assertEquals(myUser.getUserName(),myCandidate.getUserName());
        log.info("Test réussi. Le mapping DAO a fonctionné correctement : les noms d'utilisateurs correspondent.");

        // Teste si le mapping DAO a bien fonctionné. Le mot de passe crypté de l'utilisateur doit correspondre au mot de passe clair.
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(),myCandidate.getPassWord()));
        log.info("Test réussi. Le mapping DAO a fonctionné correctement : le mot de passe encodé correspond au mot de passe en clair.");
    }

    /**
     * Teste la création d'un utilisateur dont le nom d'utilisateur existe dejà.
     */
    @Test
    public void createTwiceSameUser() {

        AppUser myTestingUser = getAdminUser();

        // On mocke la DAO avec l'utilisateur à tester
        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myTestingUser);

        UserAlreadyExistsException result = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(myTestingUser);
        });

        log.info("Test réussi. Erreur levée : {}", result.getMessage());
    }
}
