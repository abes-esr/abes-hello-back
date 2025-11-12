package fr.abes.helloabes.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AppUserDto {

    @JsonProperty("userName")
    @NotNull(message = "Le nom d'utilisateur ne doit pas être null")
    @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
    @Schema(name = "userName", description = "nom de l'utilisateur", type = "String", example = "eryne")
    private String userName;

    @JsonProperty("passWord")
    @NotNull(message = "Le mot de passe ne doit pas être null")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$", message = "Le mot de passe ne respecte pas les règles de sécurité")
    @Schema(name = "passWord", description = "mot de passe de l'utilisateur", type = "String", example = "m0TdEp*ssEd!ff!c!lE*cr*queR")
    private String passWord;

}
