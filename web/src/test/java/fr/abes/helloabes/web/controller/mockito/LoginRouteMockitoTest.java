package fr.abes.helloabes.web.controller.mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /login
 */
public class LoginRouteMockitoTest extends PublicControllerMockitoTestBase {

    /**
     * Test la route /login avec la méthode GET
     * @throws Exception
     */
    @Test
    public void loginGetMethod() throws Exception {
        mockMvc.perform(get("/api/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /login avec la méthode POST
     * @throws Exception
     */
    @Test
    public void loginPostMethod() throws Exception {
        mockMvc.perform(post("/api/login"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /login avec la méthode PUT
     * @throws Exception
     */
    @Test
    public void loginPutMethod() throws Exception {
        mockMvc.perform(put("/api/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /login avec la méthode DELETE
     * @throws Exception
     */
    @Test
    public void loginDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
    
    /**
     * Test l'authentification d'un utilisateur
     * @return String le token généré
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void loginAdminUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(myDataBaseUser);

         mockMvc.perform(post("/api/login")
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
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void loginWrongUserNameUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser("test",myUser.getPassWord());

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName(myTestingUser.getUserName())).thenReturn(null);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test l'authentification d'un utilisateur avec un mot de passe incorrect
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void loginWrongPassWordUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),"@testTest123");

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test l'authentification avec un JSON vide
     * @throws Exception
     */
    @Test
    public void loginEmptyJson() throws Exception {

        String json = "{}";

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());

    }
}
