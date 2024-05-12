package de.hbrs.se2.model.company;

import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Encryption;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CompanyTest {
    private Company com;
    private User user;
    private Advertisement ad;
    @BeforeEach
    void setUp(){
        user = User.builder().email("test@test.te").password(Encryption.sha256("test")).build();
       // com = new Company("Testcom",null,"spielen","testen","0123456789",user);
        ad = new Advertisement();
        ad.setName("hallo");
    }
    @Test
    public void testgetName(){
        assertEquals("Testcom", com.getName());
    }
    @Test
    public void testgetLogo(){
        assertNull(com.getLogo());
    }
    @Test
    public void testgetIndustry(){
        assertEquals("spielen", com.getIndustry());
    }
    @Test
    public void testgetDescription(){
        assertEquals("testen", com.getDescription());
    }
    @Test
    public void testgetNumber(){
        assertEquals("0123456789", com.getPhoneNumber());
    }
    @Test
    public void testgetUser(){
        assertEquals(user, com.getUser());
    }
   // @Test
//    public void testsetgetAdvertisment(){
//        com.addAdvertisement(ad);
//        assertEquals(ad, com.getAdvertisements("hallo"));
//
//    }

}