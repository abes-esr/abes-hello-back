package fr.abes.helloabes.core.repository;

import fr.abes.helloabes.core.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<Users, String> {

    Users findUserByUserName(String userName);
}
