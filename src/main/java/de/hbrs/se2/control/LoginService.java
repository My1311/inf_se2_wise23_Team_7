package de.hbrs.se2.control;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.router.RouteConfiguration;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import de.hbrs.se2.util.Encryption;
import de.hbrs.se2.views.routes.loggedin.LoggedInStudent;
import de.hbrs.se2.views.routes.login.Login;
import de.hbrs.se2.views.routes.profile.CompanyProfile;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LoginService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Getter
    @Setter
    private User user;

    @Autowired
    public LoginService(UserRepository userRepository, CompanyService companyService,
                        StudentService studentService, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public static void setRouteConfiguration() {
        RouteConfiguration.forApplicationScope().removeRoute(Login.class);
        RouteConfiguration.forApplicationScope().removeRoute(LoggedInStudent.class);
    }

    public boolean getUserByEmail(String email) {
        this.user = userRepository.getByEmail(email.toLowerCase());
        return this.user != null;
    }

    public boolean authenticate(String password) {
        if (this.user != null && this.user.getPassword().equals(Encryption.sha256(password))) {         // lieber über UserService aber fürs Erste klappt es//
            Notification.show("Input is Valid!");
            return true;
        } else {
            Notification.show("Wrong Username or Password!");
            return false;
        }
    }

    public boolean validateFieldsLogin(String emailField, String passwordField) {
        return !emailField.trim().isEmpty() && !passwordField.trim().isEmpty();
    }

    public boolean validateFieldsSecurity(String question, String answer) {
        return !question.isEmpty() && !answer.trim().isEmpty();
    }

    public boolean checkSecurityAnswer(String answer) {
        if (userService.checkSecurityAnwser(answer, this.user.getEmail())) {
            Notification.show("Successful login");
            return true;
        }
        Notification.show("Question or Answer does not match");
        return false;
    }

    public void navigate(BaseEntity identity) {
        if (identity instanceof Student) {
            directToLoggedInStudent();
        } else {
            directToLoggedInCompany();
        }
    }

    public void directToLoggedInStudent() {
        //RouteConfiguration.forApplicationScope().setAnnotatedRoute(LoggedInStudent.class);
        UI.getCurrent().navigate(LoggedInStudent.class);
    }

    public void directToLoggedInCompany() {
        //RouteConfiguration.forApplicationScope().setAnnotatedRoute(LoggedInCompany.class);
        UI.getCurrent().navigate(CompanyProfile.class);
    }

    public @Nullable User addUser(User user) {
        return this.userRepository.save(user);
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public void delete(User user) {
        this.userRepository.delete(user);
    }

    public User findUserByEmail(String email) {
        return this.userRepository.getByEmail(email);
    }
}
