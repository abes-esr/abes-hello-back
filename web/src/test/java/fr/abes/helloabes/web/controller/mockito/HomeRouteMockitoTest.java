package fr.abes.helloabes.web.controller.mockito;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /api/v1
 */
@Slf4j
public class HomeRouteMockitoTest extends PublicControllerMockitoTestBase {

    /**
     * Teste de la route /api/v1 avec :
     *  . GET
     *  Résultats :
     *  . isOk()
     *  . renvoi du message de bienvenue
     * @throws Exception Lève une exception
     */
    @Test
    public void homeGetMethod() throws Exception {
        mockMvc.perform(get("/api/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - PUBLIC API PAGE"))
                .andDo(result -> {
                    log.info("Test réussi. Résultat : {}", result.getResponse().getContentAsString());
                });
    }


    /**
     * Test la route /api/v1 avec :
     *  . POST
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void homePostMethod() throws Exception {
        mockMvc.perform(post("/api/v1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }


    /**
     * Test la route /api/v1 avec :
     *  . PUT
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void homePutMethod() throws Exception {
        mockMvc.perform(put("/api/v1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }


    /**
     * Test la route /api/v1 avec :
     *  . DELETE
     *  Résultats :
     *  . isMethodNotAllowed()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void homeDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api/v1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }


    /**
     * Test la route /api/v1 avec :
     *  . GET
     *  . un body vide
     *  Résultats :
     *  . isOk()
     *  . renvoi du message de bienvenue
     * @throws Exception Lève une exception
     */
    @Test
    public void homeEmptyBody() throws Exception {

        String json = "{ }";
        mockMvc.perform(get("/api/v1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - PUBLIC API PAGE"))
                .andDo(result -> {
                   log.info("Test réussi. Résultat : {}", result.getResponse().getContentAsString());
                });
    }
}
