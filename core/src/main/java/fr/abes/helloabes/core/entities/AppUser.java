package fr.abes.helloabes.core.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
@Table(name="app_user")
public class AppUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Identifiant unique d'un utilisateur  */
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer identityNumber;

    /** Nom d'utilisateur  */
    @Column(name = "user_name")
    @NotNull(message = "Le nom d'utilisateur ne doit pas être null")
    @NotEmpty(message = "Le nom d'utilisateur ne doit pas être vide")
    private String userName;

    /** Mot de passe */
    @Column(name = "user_password")
    @NotNull(message = "Le mot de passe ne doit pas être null")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=]).{8,}$", message = "Le mot de passe ne respecte pas les règles de sécurité")
    private String passWord;

    /** Liste des commandes de l'utilisateur */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    /**
     * Construit un utilisateur à partir de son nom d'utilisateur et de son mot de passe.
     * @param userName Chaîne de caractère du nom de l'utilisateur.
     * @param password Chaîne de caractère du mot de passe de l'utilisateur.
     */
    public AppUser(String userName, String password) {
        this.userName = userName;
        this.passWord = password;
    }

    /**
     * Ajoute une commande à l'utilisateur.
      * @param order Order la commande à ajouter.
     */
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
    }

    /**
     * Supprime une commande à l'utilisateur.
     * @param order Order la commande à suprimmer.
     */
    public void removeOrder(Order order) {
        order.setUser(null);
        this.orders.remove(order);
    }

    /**
     * Supprime toutes les commandes de l'utilisateur.
     */
    public void removeOrders() {
        Iterator<Order> iterator = this.orders.iterator();

        while (iterator.hasNext()) {
            Order order = iterator.next();

            order.setUser(null);
            iterator.remove();
        }
    }

    @Override
    public String toString() {
        return "AppUser {"+ "identityNumber="+ identityNumber +", userName ="+userName+"}";
    }
}
