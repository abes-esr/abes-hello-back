package fr.abes.helloabes.web.controller.jpa;

import fr.abes.helloabes.core.entities.AppUser;
import lombok.extern.slf4j.Slf4j;
import org.mockito.Mockito;
import org.springframework.test.annotation.IfProfileValue;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
public class SecuredRouteJPATest extends SecuredControllerJPATestBase {

    /**
     * Teste la route /api/v1/secured/command avec :
     *  . GET
     *  . authentification valide
     *  Résultats :
     *  . isOk()
     *  . renvoi la liste des commandes passées par l'utilisateur enregistré
     * @throws Exception Lève une exception
     */
    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-jpa")
    public void securedCommandeAuthenticate() throws Exception {

        AppUser adminUser = getTotoUser();
        AppUser myDataBaseUser = getDataBaseUser(adminUser);
        String token = authenticate(myDataBaseUser);

        Mockito.when(orderService.findOrdersOfUser(Mockito.any())).thenReturn(getListOfOrders(myDataBaseUser));

        mockMvc.perform(get("/api/v1/secured/commande")
                .header("Authorization","Bearer "+token))
                .andDo(result -> {
                    log.info("Test réussi. Status Code : {}", result.getResponse().getStatus());
                })
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].id").exists())
                .andExpect(jsonPath("$.[0].id").isNumber())
                .andExpect(jsonPath("$.[0].fournisseur").exists())
                .andExpect(jsonPath("$.[0].fournisseur.id").isNumber())
                .andExpect(jsonPath("$.[0].fournisseur.name").exists())
                .andExpect(jsonPath("$.[0].fournisseur.name").isString())
                .andExpect(jsonPath("$.[0].products").exists())
                .andExpect(jsonPath("$.[0].products").isArray())
                .andExpect(jsonPath("$.[0].products[0].id").isNumber())
                .andExpect(jsonPath("$.[0].products[0].name").isString())
                .andExpect(jsonPath("$.[0].products[0].price").isNumber());
    }
}
