package fr.abes.helloabes.core.controller.apisecured;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/secured")
public class SecuredController {

    @GetMapping
    public String displaySecureHome() {
        return "Hello from Abes - Voici est private API";
    }
}
