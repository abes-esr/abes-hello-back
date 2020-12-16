package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.annotation.IfProfileValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecuredRouteMockitoTest extends SecuredControllerMockitoTestBase {

    /**
     * Test la route /secured sans authentification
     * @throws Exception
     */
    @Test
    public void securedNotAuthenticate() throws Exception {

        mockMvc.perform(get("/api/secured"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/secured"));
    }

    /**
     * Test la route /secured avec authentification valide
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void securedAuthenticate() throws Exception {

        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);

        mockMvc.perform(get("/api/secured")
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

        mockMvc.perform(get("/api/secured")
                .header("Authorization","Bearer 1234"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/secured"));
    }

    /**
     * Test la route /secured avec un jeton valide mais un utilisateur qui n'existe pas
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void securedTokenUserNotFound() throws Exception {

        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);

        Mockito.when(userDao.findByUserName(myDataBaseUser.getUserName())).thenReturn(null);

        mockMvc.perform(get("/api/secured")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/secured"));

    }

    /**
     * Test la route /secured avec un jeton expiré
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-mockito")
    public void securedExpiredToken() throws Exception {

        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkFCRVMiLCJleHAiOjE2MDYyMzEyODYsImlhdCI6MTYwNjIzMTI4NX0.-09cv6DFzgFtm9b8nNNIP2EzqMOun7GScly8zWJy3dc";

        mockMvc.perform(get("/api/secured")
                .header("Authorization","Bearer "+expiredToken))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value("UNAUTHORIZED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("This ressource requires an authentification"))
                .andExpect(jsonPath("$.path").value("/api/secured"));
    }
}

