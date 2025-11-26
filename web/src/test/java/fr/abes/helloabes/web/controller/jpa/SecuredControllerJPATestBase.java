package fr.abes.helloabes.web.controller.jpa;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.impl.OrderServiceImpl;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.controller.PublicController;
import fr.abes.helloabes.web.controller.SecuredController;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;

import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ExtendWith(SpringExtension.class)
@Slf4j
public class SecuredControllerJPATestBase extends ApplicationJPATestBase {

    @InjectMocks
    protected SecuredController securedController;

    @InjectMocks
    protected PublicController publicController;

    @MockitoBean
    protected OrderServiceImpl orderService;

    @BeforeEach
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userDao,encoder()),authenticationManager,jwtUtility, dtoMapper);
        securedController = new SecuredController(new UserServiceImpl(userDao,encoder()), orderService, dtoMapper);
    }

    /**
     * Teste le chargement du contexte avec :
     *  . securedController
     *  . publicController
     *  Résultats :
     *  . isNotNull
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-jpa")
    public void contextLoads() {
        Assertions.assertNotNull(securedController);
        log.info("Test réussi. Contrôleur initialisé : {}", securedController);
        Assertions.assertNotNull(publicController);
        log.info("Test réussi. Contrôleur initialisé : {}", publicController);
    }

    /**
     * Authentifie l'utilisateur
     * @param myUser AppUser Utilisateur à enregistrer et à authentifier
     * @return String JWT
     * @throws Exception Lève une exception
     */
    protected String authenticate(AppUser myUser) throws Exception {

        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(myDataBaseUser);

        MvcResult result = mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
    }
}
