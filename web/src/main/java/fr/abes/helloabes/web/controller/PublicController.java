package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IUserService;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.configuration.AuthenticationResponse;
import fr.abes.helloabes.web.configuration.DtoMapperUtility;
import fr.abes.helloabes.web.configuration.JwtUtility;
import fr.abes.helloabes.web.dto.AppUserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Contrôleur d'API RESTful pour toutes les routes publiques.
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
    private final DtoMapperUtility dtoMapper;

    /**
     * Construit un contrôleur de l'API pour toutes les routes publiques.
     * @param userService Service pour les utilisateurs du service web.
     * @param authenticationManager Gestionnaire des authentifications.
     * @param jwtUtility Filtre pour les jetons JWT.
     */
    public PublicController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtility jwtUtility, DtoMapperUtility dtoMapper) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtility = jwtUtility;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Traitement d'une requête GET sur la route par défaut '/'.
     * @return Une collection de chaîne de caractère
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération terminée avec succès."),
            @ApiResponse(responseCode = "503", description = "Service indisponible"),
    })
    @Operation(
            summary = "Message public",
            description = "Retourne un message de bienvenue")
    public Map<String, String> displayHome() {

        return Collections.singletonMap("response", "Hello from ABES - PUBLIC API PAGE");
    }

    /**
     * Traitement d'une requête POST sur la route '/register'.
     * @param user Utilisateur du service web à enregistrer.
     * @return L'utilisateur du service web enregistré.
     */
    @PostMapping("/register")
    @Operation(
            summary = "Enregistrer un utilisateur",
            description = "Enregistre un nouvel utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération terminée avec succès."),
            @ApiResponse(responseCode = "503", description = "Service indisponible."),
            @ApiResponse(responseCode = "400", description = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronée..."),
            @ApiResponse(responseCode = "404", description = "Opération a échoué."),
    })
    public AppUserDto register(
            @Parameter(description = "Objet JSON contenant les informations sur l'utilisateur à enregistrer. Tous les champs sont nécessaire.", required = true)
            @PathParam("user")
            @Valid @NotNull @RequestBody AppUserDto user) {

        AppUser userApp = dtoMapper.map(user, AppUser.class);

        AppUser savedUser = userService.createUser(userApp);

        return dtoMapper.map(savedUser,AppUserDto.class);
    }

    /**
     * Traitement d'une requête POST sur la route '/login'.
     * @param userDto Utilisateur du service web à identifier.
     * @return ResponseEntity<AuthenticationResponse> Une réponse en JSON contant un objet AuthenticationResponse.
     */
    @PostMapping("/login")
    @Operation(
            summary = "Authentifier un utilisateur",
            description = "Service d'authentification d'un utilisateur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération terminée avec succès."),
            @ApiResponse(responseCode = "503", description = "Service indisponible."),
            @ApiResponse(responseCode = "400", description = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronée..."),
            @ApiResponse(responseCode = "404", description = "Opération a échoué."),
    })
    public ResponseEntity<AuthenticationResponse> generateToken(
            @Parameter(description = "Objet JSON contenant les informations sur l'utilisateur à authentifier. Tous les champs sont nécessaire.", required = true)
            @PathParam("user")
            @Valid @NotNull @RequestBody AppUserDto userDto) {
        AppUser authRequest = dtoMapper.map(userDto, AppUser.class);

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtUtility.generateToken(authRequest.getUserName());

        return ResponseEntity.ok(new AuthenticationResponse(token, authRequest.getUserName()));
    }
}
