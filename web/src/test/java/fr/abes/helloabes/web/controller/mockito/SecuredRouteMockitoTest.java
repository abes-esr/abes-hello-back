package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SecuredRouteMockitoTest extends SecuredControllerMockitoTestBase {

    /**
     * Teste la route /api/v1/secured avec :
     *  . GET
     *  . aucune authentification
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void securedNotAuthenticate() throws Exception {

        mockMvc.perform(get("/api/v1/secured"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured"));
    }

    /**
     * Teste la route /api/v1/secured avec :
     *  . GET
     *  . authentification valide
     *  Résultats :
     *  . isOk()
     *  . renvoi du message de bienvenue sur l'espace sécurisé
     * @throws Exception Lève une exception
     */
    @Test
    public void securedAuthenticate() throws Exception {

        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);

        mockMvc.perform(get("/api/v1/secured")
                .header("Authorization","Bearer "+token))
                .andDo(result -> {
                    log.info("Test réussi. Status Code : {}", result.getResponse().getStatus());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - {* PRIVATE *} API PAGE"));
    }

    /**
     * Teste la route /api/v1/secured avec :
     *  . GET
     *  . JWT incorrect
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void securedInvalidToken() throws Exception {

        mockMvc.perform(get("/api/v1/secured")
                .header("Authorization","Bearer 1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured"));
    }

    /**
     * Teste la route /api/v1/secured avec :
     *  . GET
     *  . utilisateur non existant
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void securedTokenUserNotFound() throws Exception {

        // Récupération d'un JWT valide
        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);

        // Mock l'appel à la base de données pour retourner un utilisateur inexistant
        Mockito.when(userDao.findByUserName(myDataBaseUser.getUserName())).thenReturn(null);

        mockMvc.perform(get("/api/v1/secured")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured"));

    }

    /**
     * Teste la route /api/v1/secured avec :
     *  . GET
     *  . JWT expiré
     *  Résultats :
     *  . isUnauthorized()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void securedExpiredToken() throws Exception {

        // Création d'un JWT expiré
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkFCRVMiLCJleHAiOjE2MDYyMzEyODYsImlhdCI6MTYwNjIzMTI4NX0.-09cv6DFzgFtm9b8nNNIP2EzqMOun7GScly8zWJy3dc";

        mockMvc.perform(get("/api/v1/secured")
                .header("Authorization","Bearer "+expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/v1/secured"));
    }
}

