package fr.abes.helloabes.core.services.serviceimpl;

import fr.abes.helloabes.core.models.AppUser;
import fr.abes.helloabes.core.repository.IUserRepository;
import fr.abes.helloabes.core.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private final IUserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AppUser createUser(AppUser user) {
        String passHash = bCryptPasswordEncoder.encode(user.getPassWord());
        user.setPassWord(passHash);
        return userRepository.save(user);
    }

    @Override
    public AppUser findUserByUserName(AppUser user) {
        return userRepository.findByUserName(user.getUserName());
    }


}
