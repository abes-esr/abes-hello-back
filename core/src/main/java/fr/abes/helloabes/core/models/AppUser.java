package fr.abes.helloabes.core.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Représente un utilisateur du service web. {@code AppUser} est synchronisé avec une base de données via Entity.
 * La framework lombok permet de générer des méthodes courantes comme Getter/Setter directement à la compilation.
 * @since 0.0.1
 * @author Duy Tran
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AppUser {

    /**
     * Identifiant unique d'un utilisateur. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private String id;

    /** Nom d'utilisateur  */
    private String userName;

    /** Mot de passe */
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

}
