package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.exception.UserAlreadyExistsException;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test d'intégration de la couche service des utilisateur de l'application.
 * La base de données est remplacée par un Mock.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplIntegrationTest {

    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Mock
    IUserDao userRepository;

    @InjectMocks
    UserServiceImpl userService;

    protected static AppUser getRandomAppUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
        return user;
    }

    @Before
    public void setup(){
        userService = new UserServiceImpl(userRepository,encoder());
    }

    @Test
    public void findUserByUserName() {

        AppUser myCandidate = getRandomAppUser();

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myCandidate);

        AppUser myUser = userService.findUserByUserName(myCandidate);

        assertEquals("admin", myUser.getUserName());

    }

    /**
     * Test le service de création d'un nouvelle utilisateur.
     * On test ici le cryptage du mot de passe.
     */
    @Test
    public void createUser() {

        AppUser myOrginalUser = getRandomAppUser();
        AppUser myTestingUser = new AppUser(myOrginalUser.getUserName(),myOrginalUser.getPassWord());
        AppUser myDataBaseUser = new AppUser(myOrginalUser.getUserName(),myOrginalUser.getPassWord());

        /**
         * Dans la base de données, le mot de passe est crypté donc
         * on encode le mot de passe de l'utilisateur original pour simuler l'enregistrement
         * dans la base de données.
         * On vérifie que le mot de passe crypté corresond au mot de passe clair.
         */
        myDataBaseUser.setPassWord(encoder().encode(myOrginalUser.getPassWord()));
        assertTrue(encoder().matches(myOrginalUser.getPassWord(), myDataBaseUser.getPassWord()));

        // On mocke la DAO avec l'utilisateur à tester et l'utilisateur de la base de données
        Mockito.when(userRepository.save(myTestingUser)).thenReturn(myDataBaseUser);

        // On appelle la fonction à tester.
        AppUser myCandidate = userService.createUser(myTestingUser);

        /**
         * On test si le mot de passe de l'utilisateur passé en paramètre
         * a bien été crypté et correspond au mot de passe clair.
         */
        assertTrue(encoder().matches(myOrginalUser.getPassWord(),myTestingUser.getPassWord()));

        /**
         * On test si le mapping DAO a bien fonctionné.
         * Les noms d'utilisateur doivent correspondrent.
         */
        Assertions.assertEquals(myOrginalUser.getUserName(),myCandidate.getUserName());

        /**
         * On test si le mapping DAO a bien fonctionné.
         * Le mot de passe crypté du candidat doit correspondre au mot de passe clair.
         */
        assertTrue(encoder().matches(myOrginalUser.getPassWord(),myCandidate.getPassWord()));
    }

    /**
     * On test la création d'un utilisateur dont le nom d'utilisateur existe dejà.
     */
    @Test
    public void createTwiceSameUser() {

        AppUser myTestingUser = getRandomAppUser();      

        // On mocke la DAO avec l'utilisateur à tester
        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myTestingUser);

        Exception exception = assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(myTestingUser);
        });

    }
}
