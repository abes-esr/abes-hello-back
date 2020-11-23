package fr.abes.helloabes.core.entities;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppUserTest {

    protected static AppUser getRandomAppUser() {
        final AppUser user;
        user = new AppUser("toto","tata");
        return user;
    }

    @Test
    public void testRandomAppUser() {
        AppUser myUser = getRandomAppUser();
        assertEquals("toto", myUser.getUserName());
        assertEquals("tata", myUser.getPassWord());
    }

    @Test
    public void testSetIdentifyNumber() {
        AppUser myUser = getRandomAppUser();
        myUser.setIdentityNumber(10);
        assertEquals(Integer.valueOf(10), Integer.valueOf(myUser.getIdentityNumber()));
    }

    @Test
    public void testSetUsername() {
        AppUser myUser = getRandomAppUser();
        myUser.setUserName("monNouveauNom");
        assertEquals("monNouveauNom", myUser.getUserName());
    }

    @Test
    public void testSetPassword() {
        AppUser myUser = getRandomAppUser();
        myUser.setPassWord("monNouveauPass");
        assertEquals("monNouveauPass", myUser.getPassWord());
    }


}
