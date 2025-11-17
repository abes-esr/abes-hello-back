package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.ApplicationTestBase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Classe de test général pour l'application.
 * Le dépôt DAO est emulé grâce au Mock.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = HelloABESApplication.class)
public class ApplicationMockitoTestBase extends ApplicationTestBase {

    @MockitoBean
    protected IUserDao userDao;

    @MockitoBean
    protected UserServiceImpl userService;
}
