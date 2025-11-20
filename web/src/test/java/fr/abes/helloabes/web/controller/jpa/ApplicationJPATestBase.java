package fr.abes.helloabes.web.controller.jpa;

import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.web.ApplicationTestBase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

/**
 * Classe de test général pour l'application.
 * Le dépôt DAO est branché directement sur la base de donnée.
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = HelloABESApplication.class)
public class ApplicationJPATestBase extends ApplicationTestBase {

    @MockitoBean
    protected IUserDao userDao;
}
