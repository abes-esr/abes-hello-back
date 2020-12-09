package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.Product;

public interface IProductService {

    /**
     * Enregistre un nouveau produit.
     * @param product Product Le produit a enregistré.
     * @return Product Le produit enregistré.
     */
    Product createProduct(Product product);

    /**
     * Recherche un produit.
     * @param product Product Le produit a recherché.
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    Product findProduct(Product product);

    /**
     * Recherche un produit par son nom.
     * @param name String Le nom du produit à rechercher.
     * @return Product Le produit ou null si le produit n'a pas été trouvé.
     */
    Product findProductByName(String name);

}
