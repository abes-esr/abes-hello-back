package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;

import java.util.List;

public interface IOrderService {

    /**
     * Enregistre une nouvelle commande.
     * @param order Order La commande a enregistré.
     * @return Order La commande enregistrée.
     */
    Order createOrder(Order order);

    /**
     * Recherche une commande.
     * @param order Order La commande a recherché.
     * @return Order La commande ou null si la commande n'a pas été trouvée.
     */
    Order findOrder(Order order);

    /**
     * Recherche les commandes de l'utilisateur.
     * @param user AppUser L'utilisateur
     * @return List<Order> Liste des commandes de l'utilisateur ou null si...
     */
    List<Order> findOrdersOfUser(AppUser user);

}
