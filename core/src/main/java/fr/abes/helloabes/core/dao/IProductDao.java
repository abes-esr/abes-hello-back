package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Représente un dépôt de Fournisseur du service web.
 * Cette classe est basée sur le framework Spring avec le module Spring Data JPA.
 * @since 0.0.1
 * @author Duy Tran
 */
@Repository
public interface IProductDao extends JpaRepository<Product, Long> {

    /**
     * Recherche un produit à partir de son identifiant.
     * @param id Long identifiant du produit
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    Product findProductById(Long id);

    /**
     * Recherche un produit à partir de son nom.
     * @param name String Nom du produit.
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Product findByName(String name);
}
