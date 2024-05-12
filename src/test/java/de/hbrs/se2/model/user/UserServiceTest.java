package de.hbrs.se2.model.user;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import de.hbrs.se2.model.common.BaseEntity;
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

public class UserServiceTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;
    private User user_student;
    private User user_company;
    private UnfinishedUser unfinishedUser;

    @BeforeEach
    void setup() {
        user_student = DefaultValues.DEFAULT_USER_STUDENT;
        user_company = DefaultValues.DEFAULT_USER_COMPANY;
        unfinishedUser = DefaultValues.DEFAULT_UNFINISHED_USER;
    }

    @Test
    void testCheckSecurityAnwser() {
        registrationService.createUserWithSecurity(user_company.getEmail(), user_company.getPassword(), user_company.getPassword()
                , "In welcher Stadt wurde deine Mutter geboren ?", "zittau");
        String answer = user_company.getSecurityAnswer();// Fehler: answer is null
        assertTrue(this.userService.checkSecurityAnwser(answer, user_company.getEmail())); // Failed
        String guessedAnswer = "random";
        assertFalse(this.userService.checkSecurityAnwser(guessedAnswer, user_company.getEmail()));
        loginService.delete(user_company);
    }

    @Test
    void testIdentifyRole() {
        // Diese Method sollte noch in  Frontend getested werden
        registrationService.createUser(user_student.getEmail(), user_student.getPassword(), user_student.getPassword());
        BaseEntity testStudentBE = userService.identifyRole(user_student);
        assertThrows(NoSuchElementException.class, () -> companyService.getCompanyByEmail(user_student.getEmail().toLowerCase()));
        assertEquals(testStudentBE.getId(), user_student.getId());
        // Falled Cannot invoke "de.hbrs.se2.model.common.BaseEntity.getId()" because "testStudentBE" is null
    }


}
