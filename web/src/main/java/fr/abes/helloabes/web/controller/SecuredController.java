package fr.abes.helloabes.web.controller;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.IOrderService;
import fr.abes.helloabes.core.service.IUserService;
import fr.abes.helloabes.web.configuration.DtoMapperUtility;
import fr.abes.helloabes.web.dto.OrderDto;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur d'API RESTful pour toutes les routes privées.
 * Cette classe est basée sur le framework Spring avec le module Spring Web.
 * @since 0.0.1
 * @author Duy Tran
 */
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/secured")
public class SecuredController {

    private final IUserService userService;

    private final IOrderService orderService;

    private final DtoMapperUtility dtoMapper;

    public SecuredController(IUserService userService, IOrderService orderService, DtoMapperUtility dtoMapper) {
        this.userService = userService;
        this.orderService = orderService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Traitement d'une requête GET sur la route '/secured'.
     * @return Map map avec la réponse.
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Message privé",
            description = "Retourne un message privé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération terminée avec succès."),
            @ApiResponse(responseCode = "503", description = "Service indisponible."),
            @ApiResponse(responseCode = "400", description = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronée..."),
            @ApiResponse(responseCode = "404", description = "Opération a échoué."),
    })
    public Map<String, String> displaySecureHome() {

        return Collections.singletonMap("response", "Hello from ABES - {* PRIVATE *} API PAGE");

    }

    /**
     * Traitement d'une requête GET sur la route '/secured'.
     * @return Liste de commande.
     */
    @GetMapping("/commande")
    @Operation(
            summary = "Liste de commandes",
            description = "Retourne une liste de tous les commandes avec les produits et le fournisseur")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Opération terminée avec succès."),
            @ApiResponse(responseCode = "503", description = "Service indisponible."),
            @ApiResponse(responseCode = "400", description = "Mauvaise requête. Le paramètre problématique sera précisé par le message d'erreur. Par exemple : paramètre manquant, adresse erronée..."),
            @ApiResponse(responseCode = "404", description = "Opération a échoué."),
    })
    public List<OrderDto> displaySecureCommandes(Authentication authentication) {

        AppUser user = userService.findUserByUserName(authentication.getName());
        return dtoMapper.mapList(orderService.findOrdersOfUser(user), OrderDto.class);

    }

}
