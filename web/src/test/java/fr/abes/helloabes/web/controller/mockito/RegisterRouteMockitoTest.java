package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.exception.UserAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /api/v1/register
 */
@Slf4j
public class RegisterRouteMockitoTest extends PublicControllerMockitoTestBase {

    /**
     * Teste la route /api/v1/register avec :
     *  . GET
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerGetMethod() throws Exception {
        mockMvc.perform(get("/api/v1/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . pas de body dans la requête
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerPostMethod() throws Exception {
        mockMvc.perform(post("/api/v1/register"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Malformed JSON request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . PUT
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerPutMethod() throws Exception {
        mockMvc.perform(put("/api/v1/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . DELETE
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1/register"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . données d'inscription correctes
     *  Résultats :
     *  . isOk()
     *  . renvoi du userName et du passWord
     * @throws Exception Lève une exception
     */
    @Test
    public void registerAdminUser() throws Exception {

        AppUser myUser = getTotoUser();
        AppUser myTestingUser = new AppUser(myUser.getUserName(),myUser.getPassWord());
        AppUser myDataBaseUser = getDataBaseUser(myUser);

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myTestingUser);
        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(null);
        Mockito.when(userService.createUser(Mockito.any())).thenReturn(myDataBaseUser);

        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value(myDataBaseUser.getUserName()))
                .andExpect(jsonPath("$.passWord").value(myDataBaseUser.getPassWord()))
                .andDo(result -> {
                    log.info("Test réussi. Status code : " + result.getResponse().getStatus());
                });
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . données d'inscription non autorisées (le userName existe déjà)
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerAlreadyExistingUser() throws Exception {
        log.info("Enregistrement d'un utilisateur via l'appel au test registerAdminUser() :");
        registerAdminUser();

        log.info("Test de l'enregistrement d'un utilisateur déjà enregistré :");
        AppUser myUser = getTotoUser();

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        Mockito.when(userDao.findByUserName(myUser.getUserName())).thenReturn(myUser);
        Mockito.when(userService.createUser(Mockito.any())).thenThrow(new UserAlreadyExistsException("Username not available"));

        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Username not available"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . données d'inscription non autorisées (le passWord ne convient pas aux exigences de sécurité)
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerWeakPassword() throws Exception {

        AppUser myUser = getTotoUser();

        myUser.setPassWord("simple");

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        Mockito.when(userDao.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . données d'inscription manquantes (body de la requête vide)
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerEmptyJson() throws Exception {

        String json = "{}";

        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste la route /api/v1/register avec :
     *  . POST
     *  . requête mal formulée (la structure du json est incorrecte)
     *  Résultats :
     *  . isBadRequest()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void registerWrongJson() throws Exception {

        String json = "{ \"username\" : \"toto\"}";

        mockMvc.perform(post("/api/v1/register")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("The credentials are not valid"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
