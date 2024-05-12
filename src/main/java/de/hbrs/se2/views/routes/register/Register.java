package de.hbrs.se2.views.routes.register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.Headerlayout.HeaderLayout;


@PageTitle("Register")
@Route(value = Constant.Value.Route.REGISTER)
public class Register extends Div {

    private final RegistrationService registrationService;
    private final Button continueButton = new Button("Continue");
    private final ComboBox<String> securityQuestions = new ComboBox<>("Sicherheitsfragen");
    private final TextField answer = new TextField();
    VerticalLayout registerview;
    private EmailField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPassword;
    private RadioButtonGroup<String> radioGroup;


    public Register(RegistrationService registrationService) {
        this.registrationService = registrationService;

        HeaderLayout headerLayout = new HeaderLayout();
        registerview = createRegistryView(registrationService);
        add(headerLayout,registerview);
    }




    public VerticalLayout createRegistryView(RegistrationService registrationService) { // registrationservice wird später benötigt

        String fieldWidth = "350px";

        emailField = new EmailField("E-Mail");
        emailField.setPlaceholder("example@example.com");
        emailField.setLabel("Please enter your email");
        emailField.addClassName("login-text-field-1");
        emailField.setRequiredIndicatorVisible(true);
        emailField.setWidth(fieldWidth);

        passwordField = new PasswordField("Password");
        passwordField.setPlaceholder("Please enter your password");
        passwordField.setLabel("Please enter your password");
        passwordField.addClassName("login-password-field-2");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setWidth(fieldWidth);

        confirmPassword = new PasswordField("Confirm password");
        confirmPassword.setPlaceholder("Please enter your password");
        confirmPassword.setLabel("Please confirm your password");
        confirmPassword.addClassName("login-password-field-3");
        confirmPassword.setRequiredIndicatorVisible(true);
        confirmPassword.setWidth(fieldWidth);

        securityQuestions.setItems("In welcher Stadt wurde deine Mutter geboren ?", "Wie lautet der Name deines ersten Haustiers ?", "Welches ist dein Lieblingsbuch ?");
        securityQuestions.setWidth(fieldWidth);

        answer.setPlaceholder("Please enter your answer here ");
        answer.setWidth(fieldWidth);

        radioGroup = new RadioButtonGroup<>();
        radioGroup.setLabel("Role");
        radioGroup.setItems("Student", "Company");
        radioGroup.addClassName("register-radio-group-1");

        continueButton.setEnabled(false);
        continueButton.addClassName("login-button-3");
        continueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        emailField.setValueChangeMode(ValueChangeMode.LAZY);
        emailField.addValueChangeListener(event -> updateUI());
        passwordField.setValueChangeMode(ValueChangeMode.LAZY);
        passwordField.addValueChangeListener(event -> updateUI());
        confirmPassword.setValueChangeMode(ValueChangeMode.LAZY);
        confirmPassword.addValueChangeListener(event -> updateUI());
        radioGroup.addValueChangeListener(event -> updateUI()); //setValueChangeMode(ValueChangeMode.LAZY); macht hier keinen Sinn

       /* continueButton.addClickListener(click -> {
            if (registrationService.checkInputRegister(
                    emailField.getValue(), passwordField.getValue(), confirmPassword.getValue(),
                    radioGroup.getValue() ,securityQuestions.getValue(), answer.getValue())) {
                if (radioGroup.getValue().equals("Student")) {
                    UI.getCurrent().navigate(RegisterStudent.class);
                } else if (radioGroup.getValue().equals("Company")) {
                    UI.getCurrent().navigate(RegisterCompany.class);
                }
            }
        });*/

        continueButton.addClickListener(click -> {
            if (registrationService.checkInputRegister(emailField.getValue(), passwordField.getValue(), confirmPassword.getValue())) {
                if (securityQuestions.getValue() == null && answer.getValue().isEmpty()) {
                    registrationService.createUser(emailField.getValue(), passwordField.getValue(), radioGroup.getValue());
                } else if (securityQuestions.getValue() != null && !answer.getValue().isEmpty()) {
                    registrationService.createUserWithSecurity(
                            emailField.getValue(), passwordField.getValue(), radioGroup.getValue(),
                            securityQuestions.getValue(), answer.getValue()
                    );
                }
                registrationService.navigate(radioGroup.getValue());
            }
        });


        Anchor existingAccount = new Anchor("/login", "Already have an account? Click here!");
        existingAccount.addClassName("register-a-1");


        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("custom-form-layout");
        verticalLayout.setWidthFull();
        verticalLayout.setHeight("700px");     // weitere Verschiebung nach unten
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(
                new H2("Register"),
                emailField,
                passwordField,
                confirmPassword,
                securityQuestions,
                answer,
                radioGroup,
                continueButton,
                existingAccount
        );
        return verticalLayout;
    }

    private void updateUI() {
        if (registrationService.validateFieldsRegister(emailField.getValue(), passwordField.getValue(), confirmPassword.getValue(), radioGroup.getValue())) {
            continueButton.setEnabled(true);
            continueButton.getStyle().set("color", "white");
        } else {
            continueButton.setEnabled(false);
        }

    }

}
