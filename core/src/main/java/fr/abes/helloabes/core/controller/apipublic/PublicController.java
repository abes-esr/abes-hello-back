package fr.abes.helloabes.core.controller.apipublic;

import fr.abes.helloabes.core.config.JwtUtil;
import fr.abes.helloabes.core.models.AppUser;
import fr.abes.helloabes.core.services.IUserService;
import fr.abes.helloabes.core.services.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

/**
 * Controlleur d'API RESTful pour toutes les routes publiques.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
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
    public Map displayHome() {
        return Collections.singletonMap("response", "Hello from ABES - PUBLIC API PAGE");
    }

    /**
     * Traitement d'une requête POST sur la route '/register'.
     * @param user Utilisateur du service web à enregistrer.
     * @return L'utilisateur du service web enregistré.
     */
    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.createUser(user);
    }

    /**
     * Traitement d'une requête POST sur la route '/login'.
     * @param authRequest Utilisateur du service web à identifier.
     * @return Jeton JWT en chaîne de caractère.
     * @throws Exception si l'authentification a échoué.
     */
    @PostMapping("/login")
    public String generateToken(@RequestBody AppUser authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord())
            );
        } catch (Exception ex) {
            throw new Exception("invalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

}
