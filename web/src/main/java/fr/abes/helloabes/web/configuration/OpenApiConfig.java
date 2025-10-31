package fr.abes.helloabes.web.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI OpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("WebService Hello Abes")
                        .description("Le service web RESTful permet d'exposer un message public de bienvenue et un message privé aux utilisateurs authentifiés sur le service.</br>" +
                                "Les échanges s'opèrent via des requêtes HTTP contenant des fichiers JSON dans le corps des requêtes (Body) et une clé 'Authorization' dans l'entête des requêtes pour les appels à des services sécurisés.")
                        .version("0.0.1")
                        .license(new License().name("CeCILL FREE SOFTWARE LICENSE AGREEMENT, Version 2.1 dated 2013-06-21"))
                        .contact(new Contact().name("Abes").url("https://github.com/abes-esr/abes-hello-back").email("scod@abes.fr")))
                .externalDocs(new ExternalDocumentation()
                        .description("Politique informatique de l'Abes")
                        .url("https://politique-informatique.abes.fr/")
                );
    }
}
