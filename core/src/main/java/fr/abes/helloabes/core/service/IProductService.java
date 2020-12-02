package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.Fournisseur;
import fr.abes.helloabes.core.entities.Products;

public interface IProductService {

    Products createProduct(Products product);
    Products FindProductById(Products product);

}
