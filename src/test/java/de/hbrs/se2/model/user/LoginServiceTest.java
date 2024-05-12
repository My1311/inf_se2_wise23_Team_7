package de.hbrs.se2.model.user;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.util.Encryption;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginServiceTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;
    private Student student;
    private Company company;
    private User user_student;
    // TEST FUR LOGIN SERVICE

    @BeforeEach
    void setup() {
        student = DefaultValues.DEFAULT_STUDENT;
        company = DefaultValues.DEFAULT_COMPANY;
        user_student = DefaultValues.DEFAULT_USER_STUDENT;
    }

    @Test
    @DisplayName("Student und Company werden erstellt und in DB hinzugefugt und spater geloescht.")
    void testInsertUser() {
        // Test passed
        User dbUser = this.loginService.addUser(this.student.getUser());
        assertEquals(this.student.getUser(), dbUser);
        Student dbStudent = this.studentService.addStudent(this.student);
        // Die Methode findStudentByUser funktioniert gut!
        assertNotNull(dbStudent);
        assertEquals(student, dbStudent);
        this.studentService.delete(student);

        User dbUser2 = this.loginService.addUser(this.company.getUser());
        assertEquals(this.company.getUser(), dbUser2);
        Company dbCompany = this.companyService.createCompany(this.company);
        assertNotNull(dbCompany);
        assertEquals(company, dbCompany);
        this.companyService.deleteCompany(company);
    }

    @Test
    void testGetUserByEmail() {
        assertFalse(this.loginService.getUserByEmail(this.student.getUser().getEmail()));
        this.loginService.addUser(this.student.getUser());
        assertTrue(this.loginService.getUserByEmail(this.student.getUser().getEmail()));
        this.studentService.delete(student);
        this.loginService.delete(student.getUser());

    }

    @Test
    void testAuthenticate() {
        // Fall 1: User ist null
        // ich kann den user nicht abmelden => ich kann nicht testen
        // assertNull(this.loginService.getUser());
        // assertFalse(this.loginService.authenticate(testStudent.getUser().getPassword()));

        // Fall 2: Password ist falsch
        //this.loginService.addUser(this.student.getUser());
        loginService.getUserByEmail(user_student.getEmail()); // um den user zu setzen
        //User testUser = loginService.getUser();
        //assertTrue(this.loginService.login(this.student.getUser().getEmail(), this.student.getUser().getPassword()));
        String falsePassword = "falschesPassword";
        assertFalse(this.loginService.authenticate(Encryption.sha256(falsePassword)));
        // Fall 3: Password ist richtig
        assertTrue(this.loginService.authenticate(user_student.getPassword()));

    }

    @Test
    void testValidateFieldsLogin() {
        // ahnlich wie testValidateFieldsSecurity
        // Fall 1: Email und Password sind leer
        assertFalse(this.loginService.validateFieldsLogin("", ""));
        // Fall 2: Email ist leer
        assertFalse(this.loginService.validateFieldsLogin("", "password"));
        // Fall 3: Password ist leer
        assertFalse(this.loginService.validateFieldsLogin("email", ""));
        // Fall 4: Email und Password sind nicht leer
        assertTrue(this.loginService.validateFieldsLogin("email", "password"));
        // Fall 5: Email und Password haben Leerzeichen
        assertTrue(this.loginService.validateFieldsLogin("email     ", "pass word    "));
    }

    @Test
    void testCheckSecurityAnswer() {
        // TODO: braucht noch kleine Methode zu überprüfen
        // Fall 1: Question und Answer sind leer
        assertTrue(this.loginService.checkSecurityAnswer(""));
        // Fall 2: Question ist leer
        assertFalse(this.loginService.checkSecurityAnswer("answer"));
        // Fall 3: Answer ist leer
        assertFalse(this.loginService.checkSecurityAnswer(""));
        // Fall 4: Question und Answer sind nicht leer
        assertTrue(this.loginService.checkSecurityAnswer("answer"));
        // Fall 5: Question und Answer haben Leerzeichen
        assertTrue(this.loginService.checkSecurityAnswer("answer     "));
    }

    @Test
    void testFindAll() {
        // Es wird viele Veranderung in DB.=> Am Ende testen
    }

    @Test
    void testFindUserByEmail() {
        // Fall 1: User ist null
        assertNull(this.loginService.findUserByEmail(this.student.getUser().getEmail()));
        // Fall 2: User ist nicht null
        this.loginService.addUser(this.student.getUser());
        assertNotNull(this.loginService.findUserByEmail(this.student.getUser().getEmail()));
        this.studentService.delete(student);
        this.loginService.delete(student.getUser());
    }

}
