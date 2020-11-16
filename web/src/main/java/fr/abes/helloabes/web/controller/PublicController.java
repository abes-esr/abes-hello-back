package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.configuration.JwtUtil;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import javax.websocket.server.PathParam;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Map;

/**
 * Controlleur d'API RESTful pour toutes les routes publiques.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/")
public class PublicController {

    /** Service pour les utilisateurs du service web.  */
    private final IUserService userService;

    /** Filtre pour les jetons JWT. */
    private final JwtUtil jwtUtil;

    /** Gestionnaire des authentifications */
    private final AuthenticationManager authenticationManager;

    /**
     * Construit un contrôlleur de l'API pour toutes les routes pupliques.
     * @param userService Service pour les utilisateurs du service web.
     * @param authenticationManager Gestionnaure des authentifications.
     * @param jwtUtil Filtre pour les jetons JWT.
     */
    @Autowired
    public PublicController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Traitement d'une requête GET sur la route par défaut '/'.
     * @return Une collection de chaîne de caractère
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(
            value = "Message public",
            notes = "Retourne un message de bienvenue")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès."),
            @ApiResponse(code = 503, message = "Service indisponible"),
    })
    public Map displayHome() {

        return Collections.singletonMap("response", "Hello from ABES - PUBLIC API PAGE");
    }

    /**
     * Traitement d'une requête POST sur la route '/register'.
     * @param user Utilisateur du service web à enregistrer.
     * @return L'utilisateur du service web enregistré.
     */
    @PostMapping("/register")
    @ApiOperation(
            value = "Enregistrer un utilisateur",
            notes = "Enregistre un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès."),
            @ApiResponse(code = 503, message = "Service indisponible."),
            @ApiResponse(code = 400, message = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronnée..."),
            @ApiResponse(code = 404, message = "Opération a échoué."),
    })
    public AppUser register(
            @ApiParam(value = "Objet JSON contenant les informations sur l'utilisateur à enregistrer. Tous les champs sont nécessairese.", required = true)
            @PathParam("user")
            @Valid @NotNull @RequestBody AppUser user) {

        return userService.createUser(user);
    }

    /**
     * Traitement d'une requête POST sur la route '/login'.
     * @param authRequest Utilisateur du service web à identifier.
     * @return Jeton JWT en chaîne de caractère.
     * @throws BadCredentialsException si l'authentification a échoué.
     */
    @PostMapping("/login")
    @ApiOperation(
            value = "Authentifier un utilisateur",
            notes = "Service d'authentification d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Opération terminée avec succès."),
            @ApiResponse(code = 503, message = "Service indisponible."),
            @ApiResponse(code = 400, message = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronnée..."),
            @ApiResponse(code = 404, message = "Opération a échoué."),
    })
    public String generateToken(
            @ApiParam(value = "Objet JSON contenant les informations sur l'utilisateur à authentifier. Tous les champs sont nécessairese.", required = true)
            @PathParam("user")
            @NotNull @RequestBody AppUser authRequest) throws BadCredentialsException {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord())
        );

        return jwtUtil.generateToken(authRequest.getUserName());
    }

}