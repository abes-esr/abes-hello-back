package fr.abes.helloabes.web.controller.jpa;

import fr.abes.helloabes.core.entities.AppUser;
import org.junit.Test;
import org.springframework.test.annotation.IfProfileValue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SecuredRouteJPATest extends SecuredControllerJPATestBase {

    /**
     * Test la route /secured/commande avec authentification valide
     * @throws Exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-jpa")
    public void securedCommandeAuthenticate() throws Exception {

        AppUser adminUser = getTotoUser();
        String token = authenticate(adminUser);

        mockMvc.perform(get("/api/secured/commande")
                .header("Authorization","Bearer "+token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].fournisseur").exists())
                .andExpect(jsonPath("$.[0].fournisseur.id").isNumber())
                .andExpect(jsonPath("$.[0].fournisseur.name").exists())
                .andExpect(jsonPath("$.[0].fournisseur.name").isString())
                .andExpect(jsonPath("$.[0]products").exists())
                .andExpect(jsonPath("$.[0]products").isArray())
                .andExpect(jsonPath("$.[0]products[0].id").isNumber())
                .andExpect(jsonPath("$.[0]products[0].name").isString())
                .andExpect(jsonPath("$.[0]products[0].price").isNumber());
    }
}

