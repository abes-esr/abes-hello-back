package fr.abes.helloabes.core.service;

import fr.abes.helloabes.core.entities.AppUser;

import java.util.List;

/**
 * Représente un service pour gérer les utilisateurs du service web.
 * Cette interface a uniquement vocation de permettre l'utilisation d'injection des dépendances 
 * du framework Spring.
 * @since 0.0.1
 * @author Duy Tran
 */
public interface IUserService {

    /**
     * Enregistre nouvel utilisateur du service web.
     * @param user Utilisateur du service web.
     * @return L'utilisateur du service web enregistré.
     */
    AppUser createUser(AppUser user);

    /**
     * Recherche un utilisateur.
     * @param user AppUser Utilisateur du service web à rechercher.
     * @return L'utilisateur du service web trouvé 
     * ou null si l'utilisateur n'a pas été trouvé.
     */
    AppUser findUser(AppUser user);

    /**
     * Recherche un utilisateur par son nom d'utilisateur.
     * @param userName String Nom de l'utilisateur
     * @return AppUser L'utilisateur trouvé 
     * ou null s' l'utilisateur n'a pas été trouvé.
     */
    AppUser findUserByUserName(String userName);

    /**
     * Récupère la liste de tous les utilisateurs.
     * @return List<AppUser> Liste des utilisateurs.
     */
    List<AppUser> getAllUsers();

}
