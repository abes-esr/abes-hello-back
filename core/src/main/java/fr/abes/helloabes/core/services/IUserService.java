package fr.abes.helloabes.core.services;

import fr.abes.helloabes.core.models.Users;

public interface IUserService {

    Users createUser(Users user);

    Users findUserByUserName(String userName);
}
