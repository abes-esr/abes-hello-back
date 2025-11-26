package fr.abes.helloabes.web.controller.mockito;

import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.controller.PublicController;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour le controlleur public.
 * On injecte le mock du dépot DAO dans le controlleur.
 */
@ExtendWith(SpringExtension.class)
@Slf4j
public class PublicControllerMockitoTestBase extends ApplicationMockitoTestBase {

    @InjectMocks
    protected PublicController publicController;

    @BeforeEach
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userDao,encoder()),authenticationManager,jwtUtility, dtoMapper);
    }

    /**
     * Teste le chargement du contexte avec :
     *  . publicController
     *  Résultats :
     *  . isNotNull
     */
    @Test
    public void contextLoads() {
        Assertions.assertNotNull(publicController);
        log.info("Test réussi. Contrôleur initialisé : {}", publicController);
    }

    /**
     * Teste une route inconnue avec :
     *  . GET
     *  Résultats :
     *  . isNotFound()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteGetMethod() throws Exception {
        mockMvc.perform(get("/test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
    /**
     * Teste une route inconnue avec :
     *  . POST
     *  Résultats :
     *  . isNotFound()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRoutePostMethod() throws Exception {
        mockMvc.perform(post("/test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste une route inconnue avec :
     *  . PUT
     *  Résultats :
     *  . isNotFound()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRoutePutMethod() throws Exception {
        mockMvc.perform(put("/test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Teste une route inconnue avec :
     *  . DELETE
     *  Résultats :
     *  . isNotFound()
     *  . renvoi d'un message d'erreur
     * @throws Exception Lève une exception
     */
    @Test
    public void wrongRouteDeleteMethod() throws Exception {
        mockMvc.perform(delete("/test"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value("NOT_FOUND"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Page not found"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }
}
