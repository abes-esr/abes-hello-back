package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Représente un dépôt de commande du service web.
 * Cette classe est basée sur le framework Spring avec le module Spring Data JPA.
 * @since 0.0.1
 * @author Duy Tran
 */
@Repository
public interface IOrderDao extends JpaRepository<Order, Long> {

    /**
     * Recherche une commande à partir de son identifiant.
     * @param id Long Identifiant de la commande.
     * @return Order La commande ou null si la commande n'a pas été trouvée.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.supplier WHERE o.id = ?1")
    Order findOrderById(Long id);

    /**
     * Recherches les commandes de l'utilisateur.
     * @param user AppUser L'utilisateur.
     * @return Liste des commandes de l'utilisateur.
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.supplier WHERE o.user = ?1")
    List<Order> findOrdersOfUser(AppUser user);
}
