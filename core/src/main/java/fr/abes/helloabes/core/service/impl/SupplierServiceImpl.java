package fr.abes.helloabes.core.service.impl;

import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.dao.ISupplierDao;
import fr.abes.helloabes.core.entities.Supplier;
import fr.abes.helloabes.core.service.ISupplierService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

public class SupplierServiceImpl implements ISupplierService {

    private final ISupplierDao supplierDao;

    public SupplierServiceImpl(ISupplierDao supplierDao) {
        this.supplierDao = supplierDao;
    }

    /**
     * Enregistre un nouveau fournisseur
     * @param supplier Supplier Le fournisseur à enregistrer.
     * @return Supplier Le fournisseur enregistré.
     */
    @Override
    public Supplier createSupplier(Supplier supplier) {
        
        return supplierDao.save(supplier);
    }

    /**
     * Recherche un fournisseur.
     * @param supplier Supplier Le fournisseur à rechercher.
     * @return Supplier le fournisseur ou null si le fournisseur n'a pas été trouvé.
     */
    @Override
    public Supplier findSupplier(Supplier supplier) {
        
        return supplierDao.findSupplierById(supplier.getId());
    }

    /**
     * Recherche un fournisseur par son nom d'utilisateur.
     * @param name String Le nom du fournisseur à rechercher.
     * @return Supplier le fournisseur ou null si le fournisseur n'a pas été trouvé.
     */
    @Override
    public Supplier findSupplierByName(String name) {

        return supplierDao.findByName(name);
    }
    
    @Transactional
    public void insertSuppliers() {
        
     
    }
}
