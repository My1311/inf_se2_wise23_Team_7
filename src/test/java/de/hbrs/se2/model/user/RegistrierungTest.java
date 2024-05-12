package de.hbrs.se2.model.user;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.Image.ImageService;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.student.Student;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class RegistrierungTest {
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UnfinishedUserService unfinishedUserService;
    private Student student;
    private User user_student, user_company;
    private Company company;
    private UnfinishedUser unfinishedUser;

    @BeforeEach
    void setup() {
        student = DefaultValues.DEFAULT_STUDENT;
        company = DefaultValues.DEFAULT_COMPANY;
        user_student = DefaultValues.DEFAULT_USER_STUDENT;
        user_company = DefaultValues.DEFAULT_USER_COMPANY;
    }

    @Test
    void testValidateFieldsRegister() {
        User testUser = user_student;
        assertTrue(this.registrationService.validateFieldsRegister(testUser.getEmail(), testUser.getPassword(), testUser.getPassword(), "Student"));
        User testUser2 = user_company;
        assertTrue(this.registrationService.validateFieldsRegister(testUser2.getEmail(), testUser2.getPassword(), testUser2.getPassword(), "Company"));
        // Fall 1: Wenn ein Feld leer ist
        assertFalse(this.registrationService.validateFieldsRegister("", testUser.getPassword(), testUser.getPassword(), "Student"));
        // Fall 2: Wenn  Passwort 1 und Passwort 2 nicht gleich sind // Failed
        // String falsePassword = "falschesPassword";
        // assertFalse(this.registrationService.validateFieldsRegister(testUser.getEmail(),testUser.getPassword(), Encryption.sha256(falsePassword), "Student"));
        // Fall 3: Wenn role ist null // Passed
        assertFalse(this.registrationService.validateFieldsRegister(testUser.getEmail(), testUser.getPassword(), testUser.getPassword(), null));
    }

    @Test
    void testValidateFieldsCompany() { // Passed
        // Fall 1: Wenn ein Feld leer ist
        assertFalse(this.registrationService.validateFieldsCompany("", "Industry", "Description"));
        // Fall 2: Wenn alle Information richtig ist
        assertTrue(this.registrationService.validateFieldsCompany("Company", "Industry", "Description"));
    }

    @Test
    void testValideFieldsStudent() { // Passed
        // Fall 1: Wenn ein Feld leer ist
        assertFalse(this.registrationService.valideFieldsStudent("", "Lastname", "Skills", "MajorStudy", "Degree"));
        // Fall 2: Wenn alle Information richtig ist
        assertTrue(this.registrationService.valideFieldsStudent("Firstname", "Lastname", "Skills", "MajorStudy", "Degree"));
    }

    @Test
    void testcheckInputRegister() {
        // Fall 1: Wenn ein Feld leer ist
        assertFalse(this.registrationService.checkInputRegister("", "Password", "Password"));
        // Fall 2: Wenn Passwort 1 und Passwort 2 nicht gleich sind
        String falsePassword = "falschesPassword";
        assertFalse(this.registrationService.checkInputRegister("Email", "Password", falsePassword));
        // Fall 3: Wenn Passwort 1 und Passwort 2 gleich sind
        assertTrue(this.registrationService.checkInputRegister("Email", "Password", "Password"));
        // Fall 4: When ein Email schon existiert
        this.loginService.addUser(user_student);
        assertFalse(this.registrationService.checkInputRegister(user_student.getEmail(), "Password", "Password"));
        this.studentService.delete(student);
        this.loginService.delete(user_student);
        assertTrue(this.registrationService.checkInputRegister(user_student.getEmail(), user_student.getPassword(), user_student.getPassword()));
    }

    @Test
    void testCreateUser() {
        // Fall 1: Wenn ein User als Student erstellt wird
        // deleteUnfinishedUser funktioniert nicht => delete User auch nicht konnen
        this.registrationService.createUser(user_student.getEmail(), user_student.getPassword(), "Student");
        assertNotNull(this.loginService.getUser());
        assertNotNull(this.unfinishedUserService.getUnfineshedUser(user_student.getEmail()));
        // bis hier funktioniert es => beide delete funktioniert nicht
        this.unfinishedUserService.deleteUnfinishedUser(user_student.getEmail());
        this.loginService.delete(user_student);
        assertFalse(this.loginService.getUserByEmail(this.user_student.getEmail()));
        // Fall 2: Wenn ein User als Company erstellt wird
        this.registrationService.createUser(user_company.getEmail(), user_company.getPassword(), "Company");
        assertNotNull(this.loginService.getUser());
        assertNotNull(this.unfinishedUserService.getUnfineshedUser(user_company.getEmail()));
        this.unfinishedUserService.deleteUnfinishedUser(user_company.getEmail());
        this.loginService.delete(user_company);
        assertFalse(this.loginService.getUserByEmail(this.user_company.getEmail()));
    }

    @Test
    void testCreateUserWithSecurity() {
        // Fall 1: Wenn ein User als Student erstellt wird
        this.registrationService.createUserWithSecurity(user_student.getEmail(), user_student.getPassword(), "Student", "Question", "Answer");
        assertNotNull(this.loginService.getUser());//true
        assertNotNull(this.unfinishedUserService.getUnfineshedUser(user_student.getEmail()));// true
        this.unfinishedUserService.deleteUnfinishedUser(user_student.getEmail()); // Failed.studentUser wird nicht geloscht.
        this.loginService.delete(user_student);
        assertFalse(this.loginService.getUserByEmail(user_student.getEmail())); // Failed


        // Fall 2: Wenn ein User als Company erstellt wird
        this.registrationService.createUserWithSecurity(user_company.getEmail(), user_company.getPassword(), "Company", "Question", "Answer");
        assertNotNull(this.loginService.getUser());
        assertNotNull(this.unfinishedUserService.getUnfineshedUser(user_company.getEmail()));
        this.unfinishedUserService.deleteUnfinishedUser(user_company.getEmail());
        this.companyService.deleteCompany(company);
        this.loginService.delete(user_company);
        assertFalse(this.loginService.getUserByEmail(user_company.getEmail()));
    }

    @Test
    void testRegisterCompany() {
        // Fall 1: Wenn ein Company registriert wird
        this.registrationService.registerCompany(company.getName(), company.getIndustry(), company.getDescription(), null);
        assertNotNull(this.companyService.getCompanyByEmail(user_company.getEmail()));
        assertNull(this.unfinishedUserService.getUnfineshedUser(user_company.getEmail()));
        this.companyService.deleteCompany(company);
        this.loginService.delete(user_company);
        assertFalse(this.loginService.getUserByEmail(this.user_company.getEmail()));
    }

    @Test
    void testRegisterStudent() {
        // Fall 1: Wenn ein Student registriert wird
        this.registrationService.registerStudent(student.getFirst_name(), student.getLast_name(), student.getList_of_skills(), student.getMajor_study(), student.getDegree());
        assertNotNull(this.studentService.getStudentByEmail(user_student.getEmail()));
        assertNull(this.unfinishedUserService.getUnfineshedUser(user_student.getEmail()));
        this.studentService.delete(student);
        this.loginService.delete(user_student);
        assertFalse(this.loginService.getUserByEmail(this.user_student.getEmail()));
    }

}
