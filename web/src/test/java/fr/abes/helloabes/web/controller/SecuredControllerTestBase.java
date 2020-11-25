package fr.abes.helloabes.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.abes.helloabes.HelloABESApplication;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.ApplicationTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
public class SecuredControllerTestBase extends ApplicationTestBase {

    @InjectMocks
    protected SecuredController securedController;
    @InjectMocks
    protected PublicController publicController;

    @Before
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userRepository,encoder()),authenticationManager,jwtUtility);
    }

    /**
     * Enregistre et authentifie l'utilisateur
     * @param myUser AppUser Utilisateur à enregistrer et à authentifier
     * @return String jeton
     * @throws Exception
     */
    protected String createAndAuthenticate(AppUser myUser) throws Exception {
      
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myDataBaseUser);

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");       
    }

    @Test
    public void contextLoads() {
        Assert.assertNotNull(securedController);
        Assert.assertNotNull(publicController);
    }
    
    /**
     * Test une route inconnue sans authentification - méthode GET
     * @throws Exception
     */
    @Test
    public void wrongRouteGetMethod() throws Exception {
        mockMvc.perform(get("/secured/test"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured/test"));
    }

    /**
     * Test une route inconnue sans authentification - méthode POST
     * @throws Exception
     */
    @Test
    public void wrongRoutePostMethod() throws Exception {
        mockMvc.perform(post("/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured/test"));
    }

    /**
     * Test une route inconnue sans authentification  - méthode PUT
     * @throws Exception
     */
    @Test
    public void wrongRoutePutMethod() throws Exception {
        mockMvc.perform(put("/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured/test"));
    }

    /**
     * Test une route inconnue sans authentification - méthode DELETE
     * @throws Exception
     */
    @Test
    public void wrongRouteDeleteMethod() throws Exception {
        mockMvc.perform(delete("/secured/test"))  .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured/test"));
    }

    /**
     * Test une route inconnue avec authentification valide - méthode GET
     * @throws Exception
     */
    @Test
    public void wrongRouteAuthenticateGetMethod() throws Exception {
        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        mockMvc.perform(get("/secured/test")
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
        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        mockMvc.perform(post("/secured/test")
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
        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        mockMvc.perform(put("/secured/test")
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
        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        mockMvc.perform(delete("/secured/test")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isNotFound()).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
