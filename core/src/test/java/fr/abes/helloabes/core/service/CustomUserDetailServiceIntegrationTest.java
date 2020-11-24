package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import static org.junit.Assert.assertEquals;

/**
 * Test d'intégration de la couche service utilisateur de Spring.
 * La base de données est remplacée par un Mock.
 */
@RunWith(MockitoJUnitRunner.class)
public class CustomUserDetailServiceIntegrationTest {

    @Mock
    IUserDao userRepository;

    @InjectMocks
    CustomUserDetailsService userService;

    @Before
    public void setup(){
        userService = new CustomUserDetailsService(userRepository);
    }

    protected static AppUser getAdminUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
        return user;
    }

    /**
     * Test la recherche d'un utilisateur par son nom d'utilisation
     */
    @Test
    public void findUserByUserName() {

        AppUser myUser = getAdminUser();

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myUser);

        UserDetails myCandidate = userService.loadUserByUsername(myUser.getUserName());

        assertEquals("admin", myCandidate.getUsername());

    }
}
