package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.Commandes;
import fr.abes.helloabes.core.entities.Fournisseur;

public interface IFournisseurService {

    Fournisseur createFournisseur(Fournisseur fournisseur);
    Fournisseur FindFournissuerById(Fournisseur fournisseur);

}
