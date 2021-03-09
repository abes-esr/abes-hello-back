package fr.abes.helloabes.web.controller.mockito;

import org.junit.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Classe de test pour la route /
 */
public class HomeRouteMockitoTest extends PublicControllerMockitoTestBase {

    /**
     * Test la route / avec la méthode GET
     * @throws Exception
     */
    @Test
    public void homeGetMethod() throws Exception {
        mockMvc.perform(get("/api"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - PUBLIC API PAGE"));
    }

    /**
     * Test la route / avec la méthode POST
     * @throws Exception
     */
    @Test
    public void homePostMethod() throws Exception {
        mockMvc.perform(post("/api"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route / avec la méthode PUT
     * @throws Exception
     */
    @Test
    public void homePutMethod() throws Exception {
        mockMvc.perform(put("/api"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }

    /**
     * Test la route / avec la méthode DELETE
     * @throws Exception
     */
    @Test
    public void homeDeleteMethod() throws Exception {
        mockMvc.perform(delete("/api"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value("METHOD_NOT_ALLOWED"))
                .andExpect(jsonPath("$.timestamp").isNotEmpty())
                .andExpect(jsonPath("$.message").value("Method is not supported for this request"))
                .andExpect(jsonPath("$.debugMessage").exists());
    }   

    /**
     * Test la route / avec la méthode GET et un JSON en body
     * @throws Exception
     */
    @Test
    public void homeEmptyBody() throws Exception {

        String json = "{ }";
        mockMvc.perform(get("/api").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Hello from ABES - PUBLIC API PAGE"));
    }
}
