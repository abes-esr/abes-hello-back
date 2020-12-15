package fr.abes.helloabes.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique d'un produit. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    /**
     * Nom du produit
     */
    @Column(name = "product_name")
    private String name;

    /**
     * Prix du produit
     */
    @Column(name = "product_price")
    private Double price;

    /**
     * La relation avec la table commande
     * Une commande peut avoir un ou plusieurs produits
     * Un produit peut êtrte ajouté dans une ou plusieurs commandes
     */
    @ManyToMany(mappedBy = "products")
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public Product(String name, Double price) {
        this.name = name;
        this.price = price;
    }

    public Product(String name, Double price, List<Order> orders) {
        this(name,price);
        this.orders = orders;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return id != null && id.equals(((Product) obj).id);
    }

    @Override
    public int hashCode() {
        return 2020;
    }

    @Override
    public String toString() {
        return "Product {" + "id=" + id + ", name=" + name + ", price=" + price + "}";
    }
}
