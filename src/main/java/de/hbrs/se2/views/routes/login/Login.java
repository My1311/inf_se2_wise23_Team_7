package de.hbrs.se2.views.routes.login;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUser;
import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.Headerlayout.HeaderLayout;
import de.hbrs.se2.views.routes.register.RegisterCompany;
import de.hbrs.se2.views.routes.register.RegisterStudent;

import java.util.NoSuchElementException;


@PageTitle("Login")
@Route(value = Constant.Value.Route.LOGIN)
public class Login extends VerticalLayout {

    static RouteConfiguration configuration = RouteConfiguration.forSessionScope();
    private final LoginService loginService;
    private final UnfinishedUserService unfinishedUserService;
    private final UserService userService;
    private final VerticalLayout loginLayout = new VerticalLayout();
    private final HorizontalLayout sideBySide = new HorizontalLayout();
    VerticalLayout loginView;
    private H2 loginTitle;
    private H2 securityTitle;
    private EmailField emailField;
    private PasswordField passwordField;
    private Anchor toRegister;
    private Paragraph forgotPassword;
    private ComboBox<String> securityQuestions;
    private TextField answer;
    private Button backToLogin;
    private Button submitLogin;
    private Button submitSecurity;

    public Login(LoginService loginService, UnfinishedUserService unfinishedUserService, UserService userService) {
        getStyle().set("padding","0");
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        HeaderLayout headerLayout = new HeaderLayout();
        headerLayout.setWidthFull();

        loginView = createLoginView(loginService);
        loginView.getStyle();
        add(headerLayout,loginView);
        this.loginService = loginService;
        this.unfinishedUserService = unfinishedUserService;
        this.userService = userService;
    }


    public VerticalLayout createLoginView(LoginService loginService) {


        loginTitle = new H2("Login");
        securityTitle = new H2("Security Question");
        securityTitle.setVisible(false);

        emailField = new EmailField("E-Mail");
        emailField.setPlaceholder("example@example.com");
        emailField.setLabel("Please enter your email");
        emailField.setRequiredIndicatorVisible(true);
        emailField.addClassName("login-text-field-1");
        emailField.setWidth("350px");


        passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Please enter your password");
        passwordField.setLabel("Please enter your password");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.addClassName("login-password-field-1");
        passwordField.setWidth("350px");
        passwordField.setValueChangeMode(ValueChangeMode.LAZY);

        toRegister = new Anchor("/register", "No account? Click here!");
        toRegister.addClassName("login-a-1");

        forgotPassword = new Paragraph("Forgot password");
        Anchor a = new Anchor();
        forgotPassword.add(a);
        forgotPassword.addClickListener(e -> showSecurityQuestion());

        forgotPassword.addClassName("forgotPassword");
        forgotPassword.add();


        securityQuestions = new ComboBox<>("Sicherheitsfragen");
        securityQuestions.addClassName("login-text-field-1");
        securityQuestions.setPlaceholder("choose a question");
        securityQuestions.setRequiredIndicatorVisible(true);
        securityQuestions.setItems("In welcher Stadt wurde deine Mutter geboren ?", "Wie lautet der Name deines ersten Haustiers ?", "Welches ist dein Lieblingsbuch ?");
        securityQuestions.setWidth("450px");
        securityQuestions.setVisible(false);

        answer = new TextField("Your answer");
        answer.setRequiredIndicatorVisible(true);
        answer.setPlaceholder("Enter your answer here");
        answer.setWidth("450px");
        answer.setVisible(false);
        answer.setValueChangeMode(ValueChangeMode.LAZY);

        backToLogin = new Button("Back");
        backToLogin.getStyle().set("margin-top", "15px");
        backToLogin.setClassName("login-button-2");
        backToLogin.getStyle().set("color", "white");
        backToLogin.setWidth("217px");
        backToLogin.addClickListener(event -> showLogin());

        submitSecurity = new Button("Submit");
        submitSecurity.getStyle().set("margin-top", "15px");
        submitSecurity.setEnabled(false);
        submitSecurity.setClassName("login-button-2");
        submitSecurity.setWidth("217px");
        submitSecurity.getStyle().set("color", "grey");

        submitLogin = new Button("Submit");
        submitLogin.setEnabled(false);
        submitLogin.addClassName("login-button-2");
        submitLogin.addThemeVariants(ButtonVariant.LUMO_PRIMARY);


        emailField.addValueChangeListener(event -> updateUILogin());
        passwordField.addValueChangeListener(event -> updateUILogin());

        securityQuestions.addValueChangeListener(event -> updateUISecurity());
        answer.addValueChangeListener(event -> updateUISecurity());
//        forgotPassword.addClickListener(event -> showSecurityQuestion());

        submitLogin.addClickListener(event -> {
            if (loginService.getUserByEmail(emailField.getValue()) && loginService.authenticate(passwordField.getValue())) {

                try {
                    UnfinishedUser unfinishedUser = unfinishedUserService.getUnfineshedUser(emailField.getValue());
                    if (unfinishedUser.getUserType().equals("Student")) {
                        loginService.setUser(unfinishedUser.getUser());
                        UI.getCurrent().navigate(RegisterStudent.class);
                    } else if (unfinishedUser.getUserType().equals("Company")) {
                        loginService.setUser(unfinishedUser.getUser());
                        UI.getCurrent().navigate(RegisterCompany.class);
                    }
                } catch (NoSuchElementException e) {
                    BaseEntity identity = userService.identifyRole(loginService.getUser());
                    loginService.navigate(identity);
                }
            }
        });

        submitSecurity.addClickListener(event -> {
            if (loginService.getUserByEmail(emailField.getValue()) && loginService.checkSecurityAnswer(answer.getValue())) {
                BaseEntity identity = userService.identifyRole(loginService.getUser());
                loginService.navigate(identity);
            }
        });

        sideBySide.setVisible(false);
        sideBySide.add(backToLogin, submitSecurity);

        loginLayout.addClassName("login-button-1");
        loginLayout.setWidthFull();
        loginLayout.setHeight("600px");     // weitere Verschiebung nach unten
        loginLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        loginLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        loginLayout.add(
                loginTitle,
                securityTitle,
                emailField,
                passwordField,
                forgotPassword,
                toRegister,
                securityQuestions,
                answer,
                submitLogin,
                sideBySide
        );

        loginLayout.setHeight("500px");

        return loginLayout;
    }

    private void updateUILogin() {
        if (loginService.validateFieldsLogin(emailField.getValue(), passwordField.getValue())) { //Todo boolean für button entfernen und so abfragen
            submitLogin.setEnabled(true);
            submitLogin.getStyle().set("color", "white");
        }
    }

    private void updateUISecurity() {
        if (emailField.getValue().isEmpty()) {
            Notification.show("please enter your email first");
            return;
        }
        if (loginService.validateFieldsSecurity(securityQuestions.getValue(), answer.getValue())) {
            submitSecurity.setEnabled(true);
            submitSecurity.getStyle().set("color", "white");
        }
    }

    private void showSecurityQuestion() {
        loginTitle.setVisible(false);
        passwordField.setVisible(false);
        forgotPassword.setVisible(false);
        toRegister.setVisible(false);
        submitLogin.setVisible(false);

        emailField.setWidth("450px");

        securityTitle.setVisible(true);
        securityQuestions.setVisible(true);
        answer.setVisible(true);
        sideBySide.setVisible(true);
    }

    private void showLogin() {
        securityTitle.setVisible(false);
        securityQuestions.setVisible(false);
        answer.setVisible(false);
        sideBySide.setVisible(false);

        emailField.setWidth("350px");

        loginTitle.setVisible(true);
        passwordField.setVisible(true);
        forgotPassword.setVisible(true);
        toRegister.setVisible(true);
        submitLogin.setVisible(true);
    }

}

