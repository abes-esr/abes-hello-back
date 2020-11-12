package fr.abes.helloabes.web.controller.apisecured;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur d'API RESTful pour toutes les routes privées.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
@RestController
@RequestMapping("/secured")
public class SecuredController {

    /**
     * Traitement d'une requête GET sur la route '/secured'.
     * @return Une chaîne de caractère.
     */
    @GetMapping
    public String displaySecureHome() {
        return "Hello from Abes - Voici est private API";
    }
}
