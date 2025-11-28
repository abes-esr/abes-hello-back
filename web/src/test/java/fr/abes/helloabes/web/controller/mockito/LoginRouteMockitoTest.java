package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;

import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /api/v1/login
 */
@Slf4j
public class LoginRouteMockitoTest extends PublicControllerMockitoTestBase {

     /**
     * Teste la route /api/v1/login avec :
     *  . GET
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginGetMethod() throws Exception {
        mockMvc.perform(get("/api/v1/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . POST
     *  . pas de body dans la requête
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginPostMethod() throws Exception {
        mockMvc.perform(post("/api/v1/login"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . PUT
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginPutMethod() throws Exception {
        mockMvc.perform(put("/api/v1/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . DELETE
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1/login"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . POST
     *  . données de connexion correctes
     *  Résultats :
     *  . isOk()
     *  . renvoi du userName et d'un token
     * @throws Exception Lève une exception
     */
    @Test
    public void loginAdminUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(myDataBaseUser);

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.userName").value(myUser.getUserName()))
                .andDo(result -> {
                    log.info("Test réussi. Status Code : {}", result.getResponse().getStatus());
                })
                .andReturn();
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . POST
     *  . données de connexion incorrectes (userName incorrect)
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginWrongUserNameUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser("test",myUser.getPassWord());

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName(myTestingUser.getUserName())).thenReturn(null);

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . POST
     *  . données de connexion incorrectes (passWord incorrect)
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginWrongPassWordUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),"@testTest123");

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);

        Mockito.when(userDao.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/login avec :
     *  . POST
     *  . données de connexion manquantes (body de la requête vide)
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void loginEmptyJson() throws Exception {
        String json = "{}";

        mockMvc.perform(post("/api/v1/login")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
