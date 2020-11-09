package fr.abes.helloabes.core.controller.apipublic;

import fr.abes.helloabes.core.config.JwtUtil;
import fr.abes.helloabes.core.models.AppUser;
import fr.abes.helloabes.core.services.IUserService;
import fr.abes.helloabes.core.services.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/")
public class PublicController {

    private final IUserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public PublicController(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Map displayHome() {
        return Collections.singletonMap("response", "Hello from ABES - PUBLIC API PAGE");
    }

    @PostMapping("/register")
    public AppUser register(@RequestBody AppUser user) {
        return userService.createUser(user);
    }

    @PostMapping("/login")
    public String generateToken(@RequestBody AppUser authRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), authRequest.getPassWord())
            );
        } catch (Exception ex) {
            throw new Exception("invalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }

}
