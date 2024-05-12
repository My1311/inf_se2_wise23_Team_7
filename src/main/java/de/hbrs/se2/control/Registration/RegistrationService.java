package de.hbrs.se2.control.Registration;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import de.hbrs.se2.control.Image.ImageService;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.student.StudentRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import de.hbrs.se2.views.routes.register.RegisterCompany;
import de.hbrs.se2.views.routes.register.RegisterStudent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final StudentRepository studentRepository;
    private final LoginService loginService;
    private final UnfinishedUserService unfinishedUserService;

    private final ImageService imageService;


    public RegistrationService(UserRepository userRepository, CompanyRepository companyRepository, StudentRepository studentRepository,
                               LoginService loginService, UnfinishedUserService unfinishedUserService, ImageService imageService) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.studentRepository = studentRepository;
        this.loginService = loginService;
        this.unfinishedUserService = unfinishedUserService;
        this.imageService = imageService;
    }

    public boolean validateFieldsRegister(String emailField, String passwordField, String passwordConfirm, String role) {
        return !emailField.trim().isEmpty() && !passwordField.trim().isEmpty()
                && !passwordConfirm.trim().isEmpty() && !(role == null);
    }

    public boolean validateFieldsCompany(String companyName, String industry, String description) {
        return !companyName.trim().isEmpty() && !industry.trim().isEmpty() && !description.trim().isEmpty();
    }

    public boolean valideFieldsStudent(String firstname, String lastname, String skills, String majorStudy, String degree) {
        return !firstname.trim().isEmpty() && !lastname.trim().isEmpty() && !skills.trim().isEmpty()
                && !majorStudy.trim().isEmpty() && !degree.trim().isEmpty();
    }

    public boolean checkInputRegister(String email, String password, String password2) {
        if (loginService.findAll().stream().anyMatch(User -> User.getEmail().equals(email.toLowerCase()))) {
            Notification.show("Username is taken please chose another one");
            return false;
        } else if (!password.equals(password2)) {
            Notification.show("passwords don't match");
            return false;
        }
        Notification.show("Registry confirmed");
        return true;
    }

    @Transactional
    public void createUser(String email, String password, String role) {
        loginService.setUser(userRepository.save(new User(email, password)));
        unfinishedUserService.createUnfinishedUser(email, role);
    }

    @Transactional
    public void createUserWithSecurity(String email, String password, String role, String question, String answer) {
        loginService.setUser(userRepository.save(new User(email, password, question, answer)));
        unfinishedUserService.createUnfinishedUser(email, role);
    }

    public void navigate(String role) {
        if (role.equals("Student")) {
            UI.getCurrent().navigate(RegisterStudent.class);
        } else if (role.equals("Company")) {
            UI.getCurrent().navigate(RegisterCompany.class);
        }
    }

    @Transactional
    public void registerCompany(String companyName, String industry, String description, InputStream logo) {
        Notification.show("your entry was successful");
        byte[] checkedlogo = logo != null ? imageService.inputStreamToByte(logo) : null;    // ich führe die die Methode inputStreamToByte nur aus, wenn es wirklich ein Bild gibt
        companyRepository.save(new Company(companyName, checkedlogo, industry, description, loginService.getUser()));
        unfinishedUserService.deleteUnfinishedUser(loginService.getUser().getEmail());
        loginService.directToLoggedInCompany();
    }

    @Transactional
    public void registerStudent(String firstname, String lastname, String skills, String majorStudy, String degree) {
        Notification.show("your entry was successful");
        studentRepository.save(new Student(firstname, lastname, skills, majorStudy, degree, loginService.getUser()));
        unfinishedUserService.deleteUnfinishedUser(loginService.getUser().getEmail());
        loginService.directToLoggedInStudent();
    }
}
