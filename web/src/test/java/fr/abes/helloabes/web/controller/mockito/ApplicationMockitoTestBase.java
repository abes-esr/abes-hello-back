package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.ApplicationTestBase;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Classe de test général pour l'application.
 * Le dépôt DAO est emulé grâce au Mock.
 */
public class ApplicationMockitoTestBase extends ApplicationTestBase {

    @MockitoBean
    protected IUserDao userDao;

    @MockitoBean
    protected UserServiceImpl userService;
}
