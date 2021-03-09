package fr.abes.helloabes.web.controller.mockito;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /register
 */
public class RegisterRouteMockitoTest extends PublicControllerMockitoTestBase {

    /**
     * Test la route /register avec la méthode GET
     * @throws Exception
     */
    @Test
    public void registerGetMethod() throws Exception {
        mockMvc.perform(get("/api/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /register avec la méthode POST
     * @throws Exception
     */
    @Test
    public void registerPostMethod() throws Exception {
        mockMvc.perform(post("/api/register"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /register avec la méthode PUT
     * @throws Exception
     */
    @Test
    public void registerPutMethod() throws Exception {
        mockMvc.perform(put("/api/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route /register avec la méthode DELETE
     * @throws Exception
     */
    @Test
    public void registerDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test l'enregistrement avec d'un utilisateur.
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void registerAdminUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);
            Mockito.when(userDao.findByUserName("admin")).thenReturn(null);
            Mockito.when(userDao.save(any(AppUser.class))).thenReturn(myDataBaseUser);

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identityNumber").value(myDataBaseUser.getIdentityNumber()))
                .andExpect(jsonPath("$.userName").value(myDataBaseUser.getUserName()))
                .andExpect(jsonPath("$.passWord").value(myDataBaseUser.getPassWord()));
    }

    /**
     * Test l'enregistrement d'un utilisateur avec un nom d'utilisateur qui existe déjà.
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void registerAlreadyExistingUser() throws Exception {

        registerAdminUser();

        AppUser myUser = getTotoUser();

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(myUser);

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Username not available"))
                .andExpect(jsonPath("$.debugMessage").exists());

    }

    /**
     * Test l'enregistrement d'un utilisateur avec un mot de passe faible.
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void registerWeakPassword() throws Exception {

        AppUser myUser = getTotoUser();

        myUser.setPassWord("simple");

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        Mockito.when(userDao.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());

    }

    /**
     * Test l'enregistrement d'un utilisateur avec un JSON vide.
     * @throws Exception
     */
    @Test
    public void registerEmptyJson() throws Exception {

        String json = "{}";

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());

    }

    /**
     * Test l'enregistrement d'un utilisateur avec un JSON mal formé.
     * @throws Exception
     */
    @Test
    public void registerWrongJson() throws Exception {

        String json = "{ \"username\" : \"toto\"}";

        mockMvc.perform(post("/api/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());

    }
}
