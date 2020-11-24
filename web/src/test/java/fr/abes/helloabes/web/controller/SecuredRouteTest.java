package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.mockito.Mockito;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecuredRouteTest extends SecuredControllerTestBase {

    /**
     * Test la route /secured sans authentification
     * @throws Exception
     */
    @Test
    public void securedNotAuthenticate() throws Exception {

        mockMvc.perform(get("/secured"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured"));
    }

    /**
     * Test la route /secured avec authentification valide
     * @throws Exception
     */
    @Test
    public void securedAuthenticate() throws Exception {

        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        mockMvc.perform(get("/secured")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - {* PRIVATE *} API PAGE"));
    }

    /**
     * Test la route /secured avec un jeton invalide
     * @throws Exception
     */
    @Test
    public void securedInvalidToken() throws Exception {

        mockMvc.perform(get("/secured")
                .header("Authorization","Bearer 1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured"));
    }

    /**
     * Test la route /secured avec un jeton valide mais un utilisateur qui n'existe pas
     * @throws Exception
     */
    @Test
    public void securedTokenUserNotFound() throws Exception {

        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);

        Mockito.when(userRepository.findByUserName("admin")).thenReturn(null);

        mockMvc.perform(get("/secured")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured"));

    }

    /**
     * Test la route /secured avec un jeton expiré
     * @throws Exception
     */
    @Test
    public void securedExpiredToken() throws Exception {

        AppUser adminUser = getAdminUser();
        String token = createAndAuthenticate(adminUser);
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkFCRVMiLCJleHAiOjE2MDYyMzEyODYsImlhdCI6MTYwNjIzMTI4NX0.-09cv6DFzgFtm9b8nNNIP2EzqMOun7GScly8zWJy3dc";

        mockMvc.perform(get("/secured")
                .header("Authorization","Bearer "+expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/secured"));
    }
}
