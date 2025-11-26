package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.impl.OrderServiceImpl;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.controller.PublicController;
import fr.abes.helloabes.web.controller.SecuredController;

import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@Slf4j
public class SecuredControllerMockitoTestBase extends ApplicationMockitoTestBase {

    @InjectMocks
    protected SecuredController securedController;

    @InjectMocks
    protected PublicController publicController;

    @BeforeEach
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userDao,encoder()),authenticationManager,jwtUtility, dtoMapper);
        securedController = new SecuredController(new UserServiceImpl(userDao,encoder()),new OrderServiceImpl(orderDao), dtoMapper);
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
     * Teste une route inconnue avec :
     *  . GET
     *  . aucune authentification
     *  Résultats :
     *   . isUnauthorized()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteGetMethod() throws Exception {
        mockMvc.perform(get("/api/v1/secured/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Teste une route inconnue avec :
     *  . POST
     *  . aucune authentification
     *  Résultats :
     *   . isUnauthorized()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRoutePostMethod() throws Exception {
        mockMvc.perform(post("/api/v1/secured/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Teste une route inconnue avec :
     *  . PUT
     *  . aucune authentification
     *  Résultats :
     *   . isUnauthorized()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRoutePutMethod() throws Exception {
        mockMvc.perform(put("/api/v1/secured/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Teste une route inconnue avec :
     *  . DELETE
     *  . aucune authentification
     *  Résultats :
     *   . isUnauthorized()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1/secured/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Teste une route inconnue avec :
     *  . GET
     *  . authentification valide
     *  Résultats :
     *   . isNotFound()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteAuthenticateGetMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(get("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste une route inconnue avec :
     *  . POST
     *  . authentification valide
     *  Résultats :
     *   . isNotFound()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteAuthenticatePostMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(post("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste une route inconnue avec :
     *  . PUT
     *  . authentification valide
     *  Résultats :
     *   . isNotFound()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteAuthenticatePutMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(put("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste une route inconnue avec :
     *  . DELETE
     *  . authentification valide
     *  Résultats :
     *   . isNotFound()
     *   . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteAuthenticateDeleteMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(delete("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
