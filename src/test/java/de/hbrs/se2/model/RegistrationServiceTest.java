package de.hbrs.se2.model;
import com.vaadin.flow.component.UI;
import de.hbrs.se2.control.Image.ImageService;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.student.StudentRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import de.hbrs.se2.views.routes.register.RegisterCompany;
import de.hbrs.se2.views.routes.register.RegisterStudent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private LoginService loginService;

    @Mock
    private UnfinishedUserService unfinishedUserService;

    @Mock
    private ImageService imageService;

    private RegistrationService registrationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        registrationService = new RegistrationService(userRepository, companyRepository, studentRepository,
                loginService, unfinishedUserService, imageService);
    }

    @Test
    public void testValidateFieldsRegister() {
        // Testfall: Alle Felder sind gültig
        boolean result = registrationService.validateFieldsRegister("test@example.com", "password", "password", "Student");
        assertTrue(result);

        // Testfall: Ein Feld ist leer
        result = registrationService.validateFieldsRegister("", "password", "password", "Student");
        assertFalse(result);
    }

    @Test
    public void testValidateFieldsCompany() {
        // Testfall: Alle Felder sind gültig
        boolean result = registrationService.validateFieldsCompany("Company Name", "Industry", "Description");
        assertTrue(result);

        // Testfall: Ein Feld ist leer
        result = registrationService.validateFieldsCompany("", "Industry", "Description");
        assertFalse(result);
    }

    @Test
    public void testValideFieldsStudent() {
        // Testfall: Alle Felder sind gültig
        boolean result = registrationService.valideFieldsStudent("John", "Doe", "Java, C++", "Computer Science", "Bachelor");
        assertTrue(result);

        // Testfall: Ein Feld ist leer
        result = registrationService.valideFieldsStudent("", "Doe", "Java, C++", "Computer Science", "Bachelor");
        assertFalse(result);
    }

    @Test
    public void testCheckInputRegister() {
        // Testfall: Benutzername ist verfügbar und Passwörter stimmen überein
        when(loginService.findAll()).thenReturn(List.of());
        assertTrue(registrationService.checkInputRegister("test@example.com", "password", "password"));

        // Testfall: Benutzername ist bereits vergeben
        when(loginService.findAll()).thenReturn(List.of(new User("test@example.com", "password")));
        assertFalse(registrationService.checkInputRegister("test@example.com", "password", "password"));

        // Testfall: Passwörter stimmen nicht überein
        when(loginService.findAll()).thenReturn(List.of());
        assertFalse(registrationService.checkInputRegister("test@example.com", "password", "wrongpassword"));
    }

    @Test
    public void testCreateUser() {
        // Testfall: Benutzer wird erfolgreich erstellt
        registrationService.createUser("test@example.com", "password", "Student");
        verify(userRepository, times(1)).save(any(User.class));
        verify(unfinishedUserService, times(1)).createUnfinishedUser("test@example.com", "Student");
    }

    @Test
    public void testCreateUserWithSecurity() {
        // Testfall: Benutzer mit Sicherheitsfrage wird erfolgreich erstellt
        registrationService.createUserWithSecurity("test@example.com", "password", "Student", "Question", "Answer");
        verify(userRepository, times(1)).save(any(User.class));
        verify(unfinishedUserService, times(1)).createUnfinishedUser("test@example.com", "Student");
    }

    @Test
    public void testNavigate() {
        // Testfall: Navigiere zu Studentenregistrierung
        registrationService.navigate("Student");
        verify(UI.getCurrent(), times(1)).navigate(RegisterStudent.class);

        // Testfall: Navigiere zu Unternehmensregistrierung
        registrationService.navigate("Company");
        verify(UI.getCurrent(), times(1)).navigate(RegisterCompany.class);
    }

    @Test
    public void testRegisterCompany() {
        // Testfall: Unternehmensregistrierung erfolgreich
        verify(companyRepository, times(1)).save(any(Company.class));
        verify(unfinishedUserService, times(1)).deleteUnfinishedUser(anyString());
        verify(loginService, times(1)).directToLoggedInCompany();
    }

    @Test
    public void testRegisterStudent() {
        // Testfall: Studentenregistrierung erfolgreich
        registrationService.registerStudent("John", "Doe", "Java, C++", "Computer Science", "Bachelor");
        verify(studentRepository, times(1)).save(any(Student.class));
        verify(unfinishedUserService, times(1)).deleteUnfinishedUser(anyString());
        verify(loginService, times(1)).directToLoggedInStudent();
    }
}
