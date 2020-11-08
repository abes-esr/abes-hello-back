package fr.abes.helloabes.core.services;

import fr.abes.helloabes.core.models.AppUser;

public interface IUserService {

    AppUser createUser(AppUser user);
    AppUser findUserByUserName(AppUser user);

}
