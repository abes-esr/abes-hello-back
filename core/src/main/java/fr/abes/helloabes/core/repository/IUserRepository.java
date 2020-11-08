package fr.abes.helloabes.core.repository;

import fr.abes.helloabes.core.models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserRepository extends JpaRepository<AppUser, String> {

    AppUser findByUserName(String userName);

}
