package fr.abes.helloabes.core.service.impl;

import fr.abes.helloabes.core.dao.IOrderDao;
import fr.abes.helloabes.core.dao.IProductDao;
import fr.abes.helloabes.core.entities.Product;
import fr.abes.helloabes.core.service.IProductService;

public class ProductServiceImpl implements IProductService {

    private final IProductDao productDao;

    public ProductServiceImpl(IProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Enregistre un nouveau produit.
     * @param product Product Le produit a enregistré.
     * @return Product Le produit enregistré.
     */
    @Override
    public Product createProduct(Product product) {
        
        return productDao.save(product);
    }

    /**
     * Recherche un produit.
     * @param product Product Le produit à rechercher.
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    @Override
    public Product findProduct(Product product) {
        
        return productDao.findProductById(product.getId());
    }

    /**
     * Recherche un produit par son nom.
     * @param name String Le nom du produit à rechercher.
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    @Override
    public Product findProductByName(String name) {
        
        return productDao.findByName(name);
    }
}
