package fr.abes.helloabes.core.service.impl;

import fr.abes.helloabes.core.dao.ICommandeDao;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.entities.Commandes;
import fr.abes.helloabes.core.service.ICommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandeServiceImpl implements ICommandeService {

    private final ICommandeDao iCommandeDao;

    public CommandeServiceImpl(ICommandeDao iCommandeDao) {
        this.iCommandeDao = iCommandeDao;
    }

    @Override
    public Commandes createCommande(Commandes commande) {
        return null;
    }

    @Override
    public Commandes findCommandeById(Commandes commande) {
        return null;
    }

    @Override
    public List<Commandes> findCommandeByUser(AppUser user) {
        return iCommandeDao.findAllByUser(user);
    }
}
