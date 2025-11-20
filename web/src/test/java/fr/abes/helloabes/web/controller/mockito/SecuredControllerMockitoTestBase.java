package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.impl.OrderServiceImpl;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.controller.PublicController;
import fr.abes.helloabes.web.controller.SecuredController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
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
     * @return String jeton
     * @throws Exception
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

    @Test
    public void contextLoads() {
        Assertions.assertNotNull(securedController);
        Assertions.assertNotNull(publicController);
    }

    /**
     * Test une route inconnue sans authentification - méthode GET
     * @throws Exception
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
     * Test une route inconnue sans authentification - méthode POST
     * @throws Exception
     */
    @Test
    public void wrongRoutePostMethod() throws Exception {
        mockMvc.perform(post("/api/v1/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Test une route inconnue sans authentification - méthode PUT
     * @throws Exception
     */
    @Test
    public void wrongRoutePutMethod() throws Exception {
        mockMvc.perform(put("/api/v1/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Test une route inconnue sans authentification - méthode DELETE
     * @throws Exception
     */
    @Test
    public void wrongRouteDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured/test"));
    }

    /**
     * Test une route inconnue avec authentification valide - méthode GET
     * @throws Exception
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
     * Test une route inconnue avec authentification valide - méthode POST
     * @throws Exception
     */
    @Test
    public void wrongRouteAuthenticatePostMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(post("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test une route inconnue avec authentification valide - méthode PUT
     * @throws Exception
     */
    @Test
    public void wrongRouteAuthenticatePutMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(put("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test une route inconnue avec authentification valide - méthode DELETE
     * @throws Exception
     */
    @Test
    public void wrongRouteAuthenticateDeleteMethod() throws Exception {
        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(delete("/api/v1/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
