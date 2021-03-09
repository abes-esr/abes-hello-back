package fr.abes.helloabes.web.controller.jpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import fr.abes.helloabes.core.entities.AppUser;
import fr.abes.helloabes.core.service.impl.OrderServiceImpl;
import fr.abes.helloabes.core.service.impl.UserServiceImpl;
import fr.abes.helloabes.web.controller.PublicController;
import fr.abes.helloabes.web.controller.SecuredController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
public class SecuredControllerJPATestBase extends ApplicationJPATestBase {

    @InjectMocks
    protected SecuredController securedController;

    @InjectMocks
    protected PublicController publicController;

    @Before
    public void setup(){
        publicController = new PublicController(new UserServiceImpl(userDao,encoder()),authenticationManager,jwtUtility);
        securedController = new SecuredController(new UserServiceImpl(userDao,encoder()),new OrderServiceImpl(orderDao));
    }

    @Test
    @IfProfileValue(name ="spring.profiles.active", value ="test-jpa")
    public void contextLoads() {
        Assert.assertNotNull(securedController);
        Assert.assertNotNull(publicController);
    }

    /**
     * Authentifie l'utilisateur
     * @param myUser AppUser Utilisateur à enregistrer et à authentifier
     * @return String jeton
     * @throws Exception
     */
    protected String authenticate(AppUser myUser) throws Exception {

        ObjectMapper Obj = new ObjectMapper();
        String json = Obj.writeValueAsString(myUser);

        MvcResult result = mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON).content(json)).andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");
    }
}
