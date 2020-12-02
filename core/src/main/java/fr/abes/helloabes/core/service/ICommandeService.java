package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Commandes;

import java.util.List;

public interface ICommandeService {

    Commandes createCommande(Commandes commande);
    Commandes findCommandeById(Commandes commande);
    List<Commandes> findCommandeByUser(AppUser user);

}
