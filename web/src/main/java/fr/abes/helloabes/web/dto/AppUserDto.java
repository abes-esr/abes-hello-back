package fr.abes.helloabes.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class AppUserDto {

    @JsonProperty("userName")
    @NotNull(message = "Le nom d'utilisateur ne doit pas être null")
    @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
    @ApiModelProperty(value = "nom de l'utilisateur", name = "userName", dataType = "String", example = "corentin")
    private String userName;

    @JsonProperty("passWord")
    @NotNull(message = "Le mot de passe ne doit pas être null")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$", message = "Le mot de passe ne respecte pas les règles de sécurité")
    @ApiModelProperty(value = "mot de passe de l'utilisateur", name = "passWord", dataType = "String", example = "motDePasseC0!mplex")
    private String passWord;

}
