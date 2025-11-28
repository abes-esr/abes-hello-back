package fr.abes.helloabes.web.controller.jpa;

import fr.abes.helloabes.core.service.IUserService;
import fr.abes.helloabes.core.service.impl.OrderServiceImpl;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.ApplicationTestBase;
import fr.abes.helloabes.web.controller.PublicController;
import fr.abes.helloabes.web.controller.SecuredController;
import fr.abes.helloabes.web.dto.AppUserDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@Slf4j
public class SecuredControllerJPATestBase extends ApplicationTestBase {

    @InjectMocks
    protected SecuredController securedController;

    @InjectMocks
    protected PublicController publicController;

    @Autowired
    protected OrderServiceImpl orderService;

    @Autowired
    protected UserServiceImpl userService;

    @Autowired
    protected IUserService iUserService;

    @BeforeEach
    public void setup(){
        publicController = new PublicController(userService, authenticationManager, jwtUtility, dtoMapper);
        securedController = new SecuredController(iUserService, orderService, dtoMapper);
    }

    /**
     * Teste le chargement du contexte avec :
     *  . securedController
     *  . publicController
     *  Résultats :
     *  . isNotNull
     */
    @Test
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
    protected String authenticate(AppUserDto myUser) throws Exception {
        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        MvcResult result = mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
    }

}
