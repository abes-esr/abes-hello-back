package fr.abes.helloabes.core.service.impl;

import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.service.IOrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final IOrderDao orderDao;

    public OrderServiceImpl(IOrderDao orderDao) {
        this.orderDao = orderDao;
    }

    /**
     * Enregistre une nouvelle commande.
     * @param order Order La commande a enregistré.
     * @return Order La commande enregistrée.
     */
    @Override
    public Order createOrder(Order order) {
        return orderDao.save(order);
    }

    /**
     * Recherche une commande.
     * @param order Order La commande a recherché.
     * @return Order La commande ou null si la commande n'a pas été trouvée.
     */
    @Override
    public Order findOrder(Order order) {
        return orderDao.findOrderById(order.getId());               
    }

    /**
     * Recherche les commandes de l'utilisateur.
     * @param user AppUser L'utilisateur
     * @return List<Order> Liste des commandes de l'utilisateur ou null si l'utilisateur n'a pas été trouvé.
     */
    @Override
    public List<Order> findOrdersOfUser(AppUser user) {
        return orderDao.findOrdersOfUser(user);
    }
}
