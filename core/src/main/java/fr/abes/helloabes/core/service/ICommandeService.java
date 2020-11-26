package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Commandes;

public interface ICommandeService {

    Commandes createCommande(Commandes commande);
    Commandes FindCommandeById(Commandes commande);

}
