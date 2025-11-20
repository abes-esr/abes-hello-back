package fr.abes.helloabes.core;

import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.dao.ISupplierDao;
import fr.abes.helloabes.core.dao.IUserDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.entities.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Test la couche Core avec la base de données.
 * Le module core ne contient aucun Boot d'application Spring car il n'est jamais exécuté seul.
 * Les annotations suivantes permettent de simuler un Boot de Spring. *
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest // Permet d'utiliser une base de données H2 en mémoire pour les tests.
@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
@EntityScan(basePackages = "fr.abes.helloabes.core.entities")
@ComponentScan(basePackages = {"fr.abes.helloabes.core.dao", "fr.abes.helloabes.core.repositories"})
@ContextConfiguration(classes = { CoreApplicationTest.class })
public class CoreApplicationTest {

    public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private IUserDao userDao;

    @Autowired
    private ISupplierDao supplierDao;

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IProductDao productDao;

    /**
     * Test l'insertion de commandes contenant des produits.
     */
    @Test
    public void insertOrders() {

        Supplier supplier = new Supplier("Abes");
        supplierDao.save(supplier);

        AppUser admin = new AppUser("admin","@adminADMIN1234");
        userDao.save(admin);

        Product book = new Product("Livre Harry Potter", 17.80);
        Product book2 = new Product("Livre Energie vagabonde", 28.40);

        Order myOrder = new Order(admin,supplier);
        myOrder.addProduct(book);
        myOrder.addProduct(book2);
        myOrder.addProduct(book);

        Order myOrder2 = new Order(admin,supplier);
        myOrder2.addProduct(book2);

        orderDao.save(myOrder);
        orderDao.save(myOrder2);

        List<Order> adminOrders = orderDao.findOrdersOfUser(admin);
        Assertions.assertEquals(2,adminOrders.size());
        Assertions.assertEquals(myOrder,adminOrders.get(0));
        Assertions.assertEquals(myOrder2,adminOrders.get(1));
    }

    /**
     * Test l'insertion de commandes ne contenant pas de produits.
     */
    @Test
    public void insertOrdersWithoutProducts() {

        Supplier supplier = new Supplier("Abes");
        supplierDao.save(supplier);

        AppUser admin = new AppUser("admin","@adminADMIN1234");
        userDao.save(admin);

        Order myOrder = new Order(admin,supplier);
        Order myOrder2 = new Order(admin,supplier);

        orderDao.save(myOrder);
        orderDao.save(myOrder2);

        List<Order> adminOrders = orderDao.findOrdersOfUser(admin);
        Assertions.assertEquals(2,adminOrders.size());
        Assertions.assertEquals(myOrder,adminOrders.get(0));
        Assertions.assertEquals(myOrder2,adminOrders.get(1));
    }

    /**
     * Test l'insertion et la suppression d'une commande.
     * Les produits ne doivent pas être supprimé.
     */
    @Test
    @Transactional
    public void insertAndRemoveOrders() {

        Supplier supplier = new Supplier("Abes");
        supplierDao.save(supplier);

        AppUser admin = new AppUser("admin", "@adminADMIN1234");
        userDao.save(admin);

        Product book = new Product("Livre Harry Potter", 17.80);
        productDao.save(book);
        Product book2 = new Product("Livre Energie vagabonde", 28.40);
        productDao.save(book2);

        Order myOrder = new Order(admin, supplier);
        myOrder.addProduct(book);
        myOrder.addProduct(book2);
        myOrder.addProduct(book);

        orderDao.save(myOrder);
        orderDao.flush();

        admin.removeOrders();
        userDao.flush();

        Assertions.assertEquals(0,admin.getOrders().size());
    }

    /**
     * Test la suppresion d'un produit qui concerne une commande.
     */
    @Test
    public void insertAndRemoveProduct() {

        Supplier supplier = new Supplier("Abes");
        supplierDao.save(supplier);

        AppUser admin = new AppUser("admin","@adminADMIN1234");
        userDao.saveAndFlush(admin);

        Product book = new Product("Livre Harry Potter", 17.80);
        Product book2 = new Product("Livre Energie vagabonde", 28.40);

        Order myOrder1 = new Order(admin,supplier);
        myOrder1.addProduct(book);
        myOrder1.addProduct(book2);
        myOrder1.addProduct(book);

        Order myOrder2 = new Order(admin,supplier);
        myOrder2.addProduct(book);
        myOrder2.addProduct(book2);

        orderDao.saveAndFlush(myOrder1);
        orderDao.saveAndFlush(myOrder2);

        myOrder1.removeProduct(book2);
        orderDao.flush();

        List<Order> orders = orderDao.findOrdersOfUser(admin);
        Assertions.assertEquals(2,orders.size());
        Assertions.assertEquals(orders.get(0),myOrder1);
    }
}
