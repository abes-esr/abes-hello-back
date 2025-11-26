package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.Supplier;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


/**
 * Test la couche DAO de l'entité AppUser avec la base de données.
 * Les tests ne concernent que l'insertion et la lecture.
 * Le module core ne contient aucun Boot d'application Spring car il n'est jamais exécuté seul.
 * Les annotations suivantes permettent de simuler un Boot de Spring.
 * @since 0.0.1
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@DataJpaTest // Permet d'utiliser une base de données H2 en mémoire pour les tests.
@EnableAutoConfiguration
@EntityScan(basePackages = "fr.abes.helloabes.core.entities")
@ContextConfiguration(classes = Supplier.class)
public class ISupplierDaoTest {

    @Autowired
    private ISupplierDao supplierDao;

    protected static Supplier getSupplier() {
        final Supplier supplier;
        supplier = new Supplier("Abes");

        return supplier;
    }

    /**
     * Test l'ajout d'un fournisseur
     */
    @Test
    public void saveSupplier() {
        Supplier transientSupplier = getSupplier();
        Supplier persistentSupplier = supplierDao.save(transientSupplier);
        Assertions.assertEquals(transientSupplier,persistentSupplier);

        log.info("Test réussi. Résultat : {}", supplierDao.findByName(transientSupplier.getName()));
    }
}
