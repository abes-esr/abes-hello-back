package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.ApplicationTestBase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour le controlleur publique.
 * On injecte le mock du dépot DAO dans le controlleur. 
 */
@RunWith(SpringRunner.class)
public class PublicControllerTestBase extends ApplicationTestBase {

    @InjectMocks
    protected PublicController publicController;

    @Before
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userRepository,encoder()),authenticationManager,jwtUtility);
    }

    @Test
    public void contextLoads() {
        Assert.assertNotNull(publicController);
    }

    /**
     * Test une route inconnue avec la methode GET
     * @throws Exception
     */
    @Test
    public void wrongRouteGetMethod() throws Exception {
        mockMvc.perform(get("/test")).andExpect(status().isNotFound());
    }

    /**
     * Test une route inconnue avec la methode POST
     * @throws Exception
     */
    @Test
    public void wrongRoutePostMethod() throws Exception {
        mockMvc.perform(post("/test")).andExpect(status().isNotFound());
    }

    /**
     * Test une route inconnue avec la methode PUT
     * @throws Exception
     */
    @Test
    public void wrongRoutePutMethod() throws Exception {
        mockMvc.perform(put("/test")).andExpect(status().isNotFound());
    }

    /**
     * Test une route inconnue avec la methode DELETE
     * @throws Exception
     */
    @Test
    public void wrongRouteDeleteMethod() throws Exception {
        mockMvc.perform(delete("/test")).andExpect(status().isNotFound());
    }    
}
