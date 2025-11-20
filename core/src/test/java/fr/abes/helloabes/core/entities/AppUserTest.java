package fr.abes.helloabes.core.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AppUserTest {

    protected static AppUser getTotoUser() {
        final AppUser user;
        user = new AppUser("toto","tata");
        return user;
    }

    /**
     * Test la création d'un utilisateur
     */
    @Test
    public void TotoUser() {
        AppUser myUser = getTotoUser();
        Assertions.assertEquals("toto", myUser.getUserName());
        Assertions.assertEquals("tata", myUser.getPassWord());
    }

    /**
     * Test la modification de l'identifiant de l'utilisateur
     */
    @Test
    public void setIdentifyNumber() {
        AppUser myUser = getTotoUser();
        myUser.setIdentityNumber(10);
        Assertions.assertEquals(Integer.valueOf(10), Integer.valueOf(myUser.getIdentityNumber()));
    }

    /**
     * Test la modification du nom d'utilisateur
     */
    @Test
    public void setUsername() {
        AppUser myUser = getTotoUser();
        myUser.setUserName("monNouveauNom");
        Assertions.assertEquals("monNouveauNom", myUser.getUserName());
    }

    /**
     * Test la modification du mot de passe
     */
    @Test
    public void setPassword() {
        AppUser myUser = getTotoUser();
        myUser.setPassWord("monNouveauPass");
        Assertions.assertEquals("monNouveauPass", myUser.getPassWord());
    }

}
