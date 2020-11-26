package fr.abes.helloabes.core.entities;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Products {


    /**
     * Identifiant unique d'un produit. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;
    @Column(name = "PRODUCT_NAME")
    private String name;
    @Column(name = "PRODUCT_PRICE")
    private Double price;

    /**
     * La relation avec la table commande
     * Une commande peut avoir un ou plusieurs produits
     * Un produit peut ajouter dans une ou plusieurs commandes
     */
    @ManyToMany(mappedBy = "products")
    private List<Commandes> commandes ;

    public Products(String name, Double price, List<Commandes> commandes) {
        this.name = name;
        this.price = price;
        this.commandes = commandes;
    }

    public Products(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
