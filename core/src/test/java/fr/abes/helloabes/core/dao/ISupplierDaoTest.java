package fr.abes.helloabes.core.dao;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Order;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.entities.Supplier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.json.JsonParseException;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test la couche DAO de l'entité AppUser avec la base de données.
 * Les tests ne concernent que l'insertion et la lecture.
 * @since 0.0.1
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest // Permet d'utiliser une base de données H2 en mémoire pour les tests.
/**
 * Le module core ne contient aucun Boot d'application Spring car il n'est jamais exécuté seul.
 * Les annotations suivantes permettent de simuler un Boot de Spring. *
 */
@EnableAutoConfiguration
@EntityScan(basePackages = "fr.abes.helloabes.core.entities")
@ContextConfiguration(classes = Supplier.class)
public class ISupplierDaoTest {

    @Autowired
    private ISupplierDao supplierDao;

    protected static Supplier getAbesSupplier() {
        final Supplier supplier;
        supplier = new Supplier("Abes");

        return supplier;
    }

    /**
     * Test l'ajout d'un fournisseur
     */
    @Test
    public void saveSupplier() {
        Supplier transientSupplier = getAbesSupplier();
        Supplier persistentSupplier = supplierDao.save(transientSupplier);
        assertEquals(transientSupplier,persistentSupplier);
    }
}
