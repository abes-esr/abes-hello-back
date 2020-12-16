package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.Supplier;
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
public interface ISupplierDao extends JpaRepository<Supplier, Long> {

    /**
     * Recherche un fournisseur à partir de son identifiant.
     * @param id Long identifiant du fournisseur.
     * @return Supplier Le fournisseur ou null si le fournisseur n'a pas été trouvé.
     */
    Supplier findSupplierById(Long id);

    /**
     * Recherche un fournisseur à partir de son nom.
     * @param name String Nom du fournisseur.
     * @return Product Le fournisseur ou null si le fournisseur n'a pas été trouvé.
     */
    @Query("SELECT s FROM Supplier s WHERE s.name = ?1")
    Supplier findByName(String name);
}
