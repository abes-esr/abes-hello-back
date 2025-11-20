package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.exception.UserAlreadyExistsException;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test d'intégration de la couche service des utilisateurs de l'application.
 * La base de données est remplacée par un Mock.
 */
@ExtendWith(SpringExtension.class)
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
     * Test la recherche d'un utilisateur par son nom d'utilisateur
     */
    @Test
    public void findUserByUserName() {

        AppUser myCandidate = getAdminUser();

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myCandidate);

        AppUser myUser = userService.findUser(myCandidate);

        Assertions.assertEquals("admin", myUser.getUserName());
    }

    /**
     * Test la création d'un nouvelle utilisateur.
     * On test ici le cryptage du mot de passe.
     */
    @Test
    public void createUser() {

        AppUser myUser = getAdminUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        // On mocke la DAO avec l'utilisateur à tester et l'utilisateur de la base de données
        Mockito.when(userRepository.save(myTestingUser)).thenReturn(myDataBaseUser);

        AppUser myCandidate = userService.createUser(myTestingUser);


        //On test si le mot de passe de l'utilisateur passé en paramètre
        //a bien été crypté et correspond au mot de passe clair.
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(),myTestingUser.getPassWord()));


        //On test si le mapping DAO a bien fonctionné.
        //Les noms d'utilisateur doivent correspondrent.
        Assertions.assertEquals(myUser.getUserName(),myCandidate.getUserName());


        //On test si le mapping DAO a bien fonctionné.
        //Le mot de passe crypté du candidat doit correspondre au mot de passe clair.
        Assertions.assertTrue(encoder().matches(myUser.getPassWord(),myCandidate.getPassWord()));
    }

    /**
     * Test la création d'un utilisateur dont le nom d'utilisateur existe dejà.
     */
    @Test
    public void createTwiceSameUser() {

        AppUser myTestingUser = getAdminUser();

        // On mocke la DAO avec l'utilisateur à tester
        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myTestingUser);

        // TODO rédiger un test correct en remplacement de celui ci-dessous :
        Exception exception = Assertions.assertThrows(UserAlreadyExistsException.class, () -> {
            userService.createUser(myTestingUser);
        });

    }
}
