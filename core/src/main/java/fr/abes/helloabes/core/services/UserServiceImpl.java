package fr.abes.helloabes.core.services;

import fr.abes.helloabes.core.models.Users;
import fr.abes.helloabes.core.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private IUserRepository userRepository;

    @Override
    public Users createUser(Users user) {
        return userRepository.save(user);
    }

    @Override
    public Users findUserByUserName(String userName) {
        return userRepository.findUserByUserName(userName);
    }
}
