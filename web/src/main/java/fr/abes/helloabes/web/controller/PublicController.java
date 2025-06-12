package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.configuration.AuthenticationResponse;
import fr.abes.helloabes.web.configuration.DtoMapperUtility;
import fr.abes.helloabes.web.configuration.JwtUtility;
import fr.abes.helloabes.web.dto.AppUserDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.websocket.server.PathParam;
import java.util.Collections;
import java.util.Map;

/**
 * Controlleur d'API RESTful pour toutes les routes publiques.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1")
public class PublicController {

    /** Service pour les utilisateurs du service web.  */
    private final IUserService userService;

    /** Filtre pour les jetons JWT. */
    private final JwtUtility jwtUtility;

    /** Gestionnaire des authentifications */
    private final AuthenticationManager authenticationManager;

    /** Service pour le mapping DTO */
    @Autowired
    private DtoMapperUtility dtoMapper;

    /**
     * Construit un contrôlleur de l'API pour toutes les routes pupliques.
     * @param userService Service pour les utilisateurs du service web.
     * @param authenticationManager Gestionnaure des authentifications.
     * @param jwtUtility Filtre pour les jetons JWT.
     */
    @Autowired
    public PublicController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtility jwtUtility) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
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
    public AppUserDto register(
            @ApiParam(value = "Objet JSON contenant les informations sur l'utilisateur à enregistrer. Tous les champs sont nécessairese.", required = true)
            @PathParam("user")
            @Valid @NotNull @RequestBody AppUserDto user) {

        AppUser userApp = dtoMapper.map(user, AppUser.class);

        AppUser savedUser = userService.createUser(userApp);

        return dtoMapper.map(savedUser,AppUserDto.class);
    }

    /**
     * Traitement d'une requête POST sur la route '/login'.
     * @param user Utilisateur du service web à identifier.
     * @return ResponseEntity<AuthenticationResponse> Une réponse en JSON contant un objet AuthentifcationResponse.
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
    public ResponseEntity<AuthenticationResponse> generateToken(
            @ApiParam(value = "Objet JSON contenant les informations sur l'utilisateur à authentifier. Tous les champs sont nécessairese.", required = true)
            @PathParam("user")
            @Valid @NotNull @RequestBody AppUserDto user) {

        AppUser authRequest = dtoMapper.map(user, AppUser.class);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord()));
        String token = jwtUtility.generateToken(authRequest.getUserName());

        return ResponseEntity.ok(new AuthenticationResponse(token, authRequest.getUserName()));
    }
}
