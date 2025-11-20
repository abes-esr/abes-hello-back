package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Test d'intégration de la couche service utilisateur de Spring.
 * La base de données est remplacée par un Mock.
 */
@ExtendWith(SpringExtension.class)
public class CustomUserDetailServiceIntegrationTest {

    @MockitoBean
    IUserDao userRepository;

    @InjectMocks
    CustomUserDetailsService userService;

    @BeforeEach
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

        Assertions.assertEquals("admin", myCandidate.getUsername());

    }
}
