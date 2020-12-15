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
@Table(name= "supplier")
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Identifiant unique d'un fournisseur. Cette identifiant est géré automatiquement par la couche
     * Entity de Java.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Long id;

    @Column(name = "supplier_name")
    private String name;

    /**
     * La relation avec la table Commandes
     */
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders = new ArrayList<>();

    public Supplier(String name, List<Order> orders) {
        this.name = name;
        this.orders = orders;
    }

    public Supplier(String name) {
        this.name = name;
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

        return id != null && id.equals(((Supplier) obj).id);
    }

    @Override
    public String toString() {
        return "Supplier {" + "id=" + id + ", name=" + name + "}";
    }
   
}
