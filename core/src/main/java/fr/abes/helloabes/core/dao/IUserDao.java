package fr.abes.helloabes.core.dao;

import fr.abes.helloabes.core.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Représente un dépôt d'utilisateur du service web.
 * Cette classe est basée sur le framework Spring avec le module Spring Data JPA.
 * @since 0.0.1
 * @author Duy Tran
 */
@Repository
public interface IUserDao extends JpaRepository<AppUser, String> {

    /**
     * Recherche un utilisateur du service web dans le dépot à partir de son nom d'utilisateur.
     * @param userName String Nom d'utilisateur.
     * @return Un utilisateur du service web ou null si l'utilisateur n'a pas été trouvé.
     */
    AppUser findByUserName(String userName);

    @Query("SELECT u FROM AppUser u LEFT JOIN FETCH u.orders o")
    List<AppUser> findAll();

}
