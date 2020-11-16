package fr.abes.helloabes.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlleur d'API RESTful pour toutes les routes privées.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/secured")
public class SecuredController {

    /**
     * Traitement d'une requête GET sur la route '/secured'.
     * @return Une chaîne de caractère.
     */
    @GetMapping
    @ApiOperation(
            value = "Message privé",
            notes = "Retourne un message privé")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opéaration terminée avec succès."),
            @ApiResponse(code = 503, message = "Service indisponible."),
            @ApiResponse(code = 400, message = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronnée..."),
            @ApiResponse(code = 404, message = "Opération a échoué."),
    })
    public String displaySecureHome() {

        return "Hello from Abes - Voici est private API";
    }
}