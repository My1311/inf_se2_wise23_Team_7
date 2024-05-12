package de.hbrs.se2.model.jobadvertisement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdvertisementTest {
    private Advertisement ad;

    @BeforeEach
    void setUp(){
        ad = new Advertisement();
    }
    @Test
    public void testsetgetName(){
        ad.setName("test");
        assertEquals("test", ad.getName());
    }
    @Test
    public void testsetgetDescription(){
        ad.setDescription("test");
        assertEquals("test", ad.getDescription());
    }
//    @Test
//    public void testsetgetJobtype(){
//        ad.setJobType("test");
//        assertEquals("test", ad.getJobType());
//    }
//    @Test
//    public void testsetgetRemote(){
//        ad.setRemote(true);
//        assertTrue(ad.isRemote());
//    }
    @Test
    public void  testsetgetActive(){
        ad.setActive(true);
        assertTrue(ad.isActive());
    }
    @Test
    public void testQuals(){
        Advertisement test;
        test = new Advertisement();
        test = ad;
        assertTrue(ad.equals(test));
    }
}