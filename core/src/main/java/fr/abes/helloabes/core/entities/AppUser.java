package fr.abes.helloabes.core.entities;

import fr.abes.helloabes.web.security.constraint.ValidPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Représente un utilisateur du service web. {@code AppUser} est synchronisé avec une base de données via Entity.
 * La framework lombok permet de générer des méthodes courantes comme Getter/Setter directement à la compilation.
 * @since 0.0.1
 * @author Duy Tran
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Table(name= "UTILISATEUR")
public class AppUser {

    /**
     * Identifiant unique d'un utilisateur. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Integer identityNumber;

    /** Nom d'utilisateur  */
    @Column(name = "USER_NAME")
    @NotNull
    @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
    private String userName;

    /** Mot de passe */
    @Column(name = "USER_PASSWORD")
    @ValidPassword
    @NotNull
    private String passWord;

    /**
     * Construit un utilisateur à partir de son nom d'utilisateur et de son mot de passe.
     * @param userName Chaîne de caractère du nom de l'utilisateur.
     * @param password Chaîne de caractère du mot de passe de l'utilisateur.
     */
    public AppUser(String userName, String password) {
        this.userName = userName;
        this.passWord = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getPassWord() {
        return this.passWord;
    }
}
