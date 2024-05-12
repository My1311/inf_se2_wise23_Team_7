package de.hbrs.se2.model.user;

import static org.junit.jupiter.api.Assertions.*;

import de.hbrs.se2.Application;


import de.hbrs.se2.util.Encryption;

import org.junit.jupiter.api.*;


import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
class UserTest {

    private User user;
    @BeforeEach
    void setUp(){
        user = User.builder().email("test@test.te").password(Encryption.sha256("test")).build();
    }


    @Test
    public void testGetEmail(){
        assertEquals("test@test.te", user.getEmail());
    }
    @Test
    public void testSetEmail(){
        user.setEmail("tester@test.te");
        assertEquals("tester@test.te", user.getEmail());
    }
    @Test
    public void testGetPassword(){
        assertEquals("test", user.getPassword());
    }
    @Test
    public void testSetPassword(){
        user.setPassword("tester");
        assertEquals("tester", user.getPassword());
    }
    @Test
    public void testCheckEmail(){
        assertTrue(user.checkEmail("test@test.te"));
    }

    @Test
    public void testHashCode() {
        assertEquals(3556498, hashCode());
    }

    @Test
    public void testEquals(){
        assertTrue(user.equals(user));
        assertFalse(user.equals(null));
        User user2 = User.builder().email("test@test3.te").password(Encryption.sha256("test2")).build();
        assertFalse(user.equals(user2));
    }
}