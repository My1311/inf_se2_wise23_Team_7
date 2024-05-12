package de.hbrs.se2.model.user;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UnfinishedUserServiceTest {

    @Autowired
    private UnfinishedUserService unfinishedUserService;

    private UnfinishedUser unfinishedUser;

    @BeforeEach
    void setup() {
        unfinishedUser = DefaultValues.DEFAULT_UNFINISHED_USER; // company
    }

    @Test
    void testCheckIfUserIsUnfinishedUser() { // Failed
        this.unfinishedUserService.createUnfinishedUser(unfinishedUser.getUser().getEmail(), unfinishedUser.getUserType());
        assertTrue(this.unfinishedUserService.checkIfUserIsUnfinishedUser(unfinishedUser.getUser().getEmail()));
        String emailNichtinDB = "myxing@gmail.com";
        assertFalse(this.unfinishedUserService.checkIfUserIsUnfinishedUser(emailNichtinDB));
        this.unfinishedUserService.deleteUnfinishedUser(unfinishedUser.getUser().getEmail());
    }

    @Test
    void testGetUnfineshedUser() {
        assertThrows(NoSuchElementException.class, () -> this.unfinishedUserService.getUnfineshedUser(unfinishedUser.getUser().getEmail()));
        this.unfinishedUserService.createUnfinishedUser(unfinishedUser.getUser().getEmail(), unfinishedUser.getUserType()); // Failed
        UnfinishedUser unfinishedUser2 = this.unfinishedUserService.getUnfineshedUser(unfinishedUser.getUser().getEmail());
        assertEquals(unfinishedUser, unfinishedUser2);
    }

    @Test
    void testCreateUnfinishedUser() {
        this.unfinishedUserService.createUnfinishedUser(unfinishedUser.getUser().getEmail(), unfinishedUser.getUserType());
        UnfinishedUser unfinishedUser2 = this.unfinishedUserService.getUnfineshedUser(unfinishedUser.getUser().getEmail());
        assertEquals(unfinishedUser, unfinishedUser2);
        this.unfinishedUserService.deleteUnfinishedUser(unfinishedUser.getUser().getEmail());
    }

    @Test
    void testDeleteUnfinishedUser() {
        this.unfinishedUserService.createUnfinishedUser(unfinishedUser.getUser().getEmail(), unfinishedUser.getUserType());
        UnfinishedUser unfinishedUser2 = this.unfinishedUserService.getUnfineshedUser(unfinishedUser.getUser().getEmail());
        assertEquals(unfinishedUser, unfinishedUser2);
        this.unfinishedUserService.deleteUnfinishedUser(unfinishedUser.getUser().getEmail());
        assertThrows(NoSuchElementException.class, () -> this.unfinishedUserService.getUnfineshedUser(unfinishedUser.getUser().getEmail()));
    }

    @Test
    void testSetUnfinishedUser(){
        unfinishedUser.setUser(DefaultValues.DEFAULT_USER_STUDENT);
        assertEquals(DefaultValues.DEFAULT_USER_STUDENT, unfinishedUser.getUser());
    }

    @Test
    void checkIfUserIsUnfinishedUserTest(){
        assertFalse(unfinishedUserService.checkIfUserIsUnfinishedUser(DefaultValues.DEFAULT_USER_STUDENT.getEmail()));
    }


}
