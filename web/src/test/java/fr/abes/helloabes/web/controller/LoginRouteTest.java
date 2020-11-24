package fr.abes.helloabes.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /login
 */
public class LoginRouteTest extends PublicControllerTestBase {

    /**
     * Test la route /login avec la méthode GET
     * @throws Exception
     */
    @Test
    public void loginGetMethod() throws Exception {
        mockMvc.perform(get("/login")).andExpect(status().isMethodNotAllowed());
    }

    /**
     * Test la route /login avec la méthode POST
     * @throws Exception
     */
    @Test
    public void loginPostMethod() throws Exception {
        mockMvc.perform(post("/login")).andExpect(status().isBadRequest());
    }

    /**
     * Test la route /login avec la méthode PUT
     * @throws Exception
     */
    @Test
    public void loginPutMethod() throws Exception {
        mockMvc.perform(put("/login")).andExpect(status().isMethodNotAllowed());
    }

    /**
     * Test la route /login avec la méthode DELETE
     * @throws Exception
     */
    @Test
    public void loginDeleteMethod() throws Exception {
        mockMvc.perform(delete("/login")).andExpect(status().isMethodNotAllowed());
    }
    
    /**
     * Test l'authentification d'un utilisateur
     * @return String le token généré
     * @throws Exception
     */
    @Test
    public void loginAdminUser() throws Exception {

        AppUser myUser = getAdminUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(myDataBaseUser);

         mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.userName").value(myUser.getUserName()));
    }

    /**
     * Test l'authentification d'un utilisateur avec un nom d'utilisateur inconnu
     * @throws Exception
     */
    @Test
    public void loginWrongUserNameUser() throws Exception {

        AppUser myUser = getAdminUser();
        AppUser myTestingUser = new AppUser("test",myUser.getPassWord());

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userRepository.findByUserName("test")).thenReturn(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").isNotEmpty());
    }

    /**
     * Test l'authentification d'un utilisateur avec un mot de passe incorrect
     * @throws Exception
     */
    @Test
    public void loginWrongPassWordUser() throws Exception {

        AppUser myUser = getAdminUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),"@testTest123");

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").isNotEmpty());
    }

    /**
     * Test l'authentification avec un JSON vide
     * @throws Exception
     */
    @Test
    public void loginEmptyJson() throws Exception {

        String json = "{}";

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").isNotEmpty());

    }
}
