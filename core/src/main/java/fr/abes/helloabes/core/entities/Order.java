package fr.abes.helloabes.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name= "order_")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique d'une commande. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    /**
     * La relation avec la table User
     * un User peut avoir 1 ou plusieurs commandes
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private AppUser user;

    /**
     * La relation avec la table fournisseur
     * un fournisseur peut fournir une ou plusieurs commandes
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="supplier_id")
    private Supplier supplier;

    /**
     * La relation avec la table commande
     * Une commande peut avoir un ou plusieurs produits
     * Un produit peut être ajouté dans une ou plusieurs commandes
     * On ne supprime pas les produits si une commande est supprimée.
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name =  "order_product",
            joinColumns  = {@JoinColumn(name="order_id")},
            inverseJoinColumns = {@JoinColumn(name="product_id" )}
    )
    private List<Product> products = new ArrayList<>();

    public Order(AppUser user, Supplier supplier) {
        this.user = user;
        this.supplier = supplier;
    }

    public Order(AppUser user, Supplier supplier, List<Product> products) {
        this(user,supplier);
        
        Iterator<Product> iterator = products.iterator();
        while (iterator.hasNext()) { 
            Product product = iterator.next();
            addProduct(product);
        }        
    }

    /**
     * Ajoute un produit à la commande.
     * @param product Product le produit à ajouter.
     */
    public void addProduct(Product product) {
        this.products.add(product);
        product.getOrders().add(this);
    }

    /**
     * Supprime un produit d'une commande.
     * @param product Product le produit à suprimmer.
     */
    public void removeProduct(Product product) {
        this.products.remove(product);
        product.getOrders().remove(this);

    }

    /**
     * Supprime tous les produits d'une commande.
     */
    public void removeProducts() {
        Iterator<Product> iterator = this.products.iterator();

        while (iterator.hasNext()) {
            Product product = iterator.next();
            
            product.getOrders().remove(this);
            iterator.remove();
        }
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
        
        return id != null && id.equals(((Order) obj).id);
    }
    
    @Override
    public int hashCode() {
        return 2020;
    }
    
    @Override
    public String toString() {
        return "Order {" + "id=" + id + "}";
    }

}
