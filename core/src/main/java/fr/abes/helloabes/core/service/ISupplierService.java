package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.Supplier;

public interface ISupplierService {

    /**
     * Enregistre un nouveau fournisseur
     * @param supplier Supplier Le fournisseur a enregistré.
     * @return Supplier Le fournisseur enregistré.
     */
    Supplier createSupplier(Supplier supplier);

    /**
     * Recherche un fournisseur.
     * @param supplier Supplier Le fournisseur a recherché.
     * @return Supplier le fournisseur ou null si le fournisseur n'a pasq été trouvé.
     */
    Supplier findSupplier(Supplier supplier);

    /**
     * Recherche un fournisseur par son nom d'utilisateur.
     * @param name String Le nom du fournisseur à rechercher.
     * @return Supplier le fournisseur ou null si le fournisseur n'a pas été trouvé.
     */
    Supplier findSupplierByName(String name);

}
