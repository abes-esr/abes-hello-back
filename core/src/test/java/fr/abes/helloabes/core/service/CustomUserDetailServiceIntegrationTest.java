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

    protected static AppUser getRandomAppUser() {
        final AppUser user;
        user = new AppUser("admin", "@totoTOTO1234");
        return user;
    }

    @Before
    public void setup(){
        userService = new CustomUserDetailsService(userRepository);
    }

    @Test
    public void findUserByUserName() {

        AppUser myCandidate = getRandomAppUser();

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myCandidate);

        UserDetails myUser = userService.loadUserByUsername(myCandidate.getUserName());

        assertEquals("admin", myUser.getUsername());

    }
}
