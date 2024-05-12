package de.hbrs.se2.views.routes.register;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.Headerlayout.HeaderLayout;

@Route(value = Constant.Value.Route.REGISTERSTUDENT/*, registerAtStartup = false*/)
public class RegisterStudent extends VerticalLayout {
    private final RegistrationService registrationService;
    VerticalLayout registercompview;
    private TextField firstname;
    private TextField lastname;
    private TextField skills;
    private TextField majorStudy;
    private TextField degree;
    private Button continueButton;

    public RegisterStudent(RegistrationService registrationService) {
        getStyle().set("padding", "0");
        this.registrationService = registrationService;
        HeaderLayout headerLayout = new HeaderLayout();
        headerLayout.setWidthFull();

        registercompview = createRegisterCompView(registrationService);
        add(headerLayout, registercompview);

    }


    public VerticalLayout createRegisterCompView(RegistrationService registrationService) {

        firstname = new TextField();
        firstname.setRequiredIndicatorVisible(true);

        lastname = new TextField();
        lastname.setRequiredIndicatorVisible(true);

        skills = new TextField();
        skills.setRequiredIndicatorVisible(true);
        skills.addClassName("company-text-field-3");

        majorStudy = new TextField();
        majorStudy.setRequiredIndicatorVisible(true);

        degree = new TextField();
        degree.setRequiredIndicatorVisible(true);


        FormLayout formLayout = new FormLayout();
        formLayout.addComponentAsFirst(firstname);
        formLayout.addFormItem(firstname, "first name");
        formLayout.addFormItem(lastname, "last name");
        formLayout.addFormItem(skills, "skills");
        formLayout.addFormItem(majorStudy, "major study");
        formLayout.addFormItem(degree, "degree");
        formLayout.setWidth("470px");
        formLayout.getStyle().set("align-self", "center");


        continueButton = new Button("continue");
        continueButton.setEnabled(false);
        continueButton.addClassName("register-student-button-1");
        continueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        continueButton.setMinWidth("150px");

        firstname.addValueChangeListener(event -> updateUI());
        lastname.addValueChangeListener(event -> updateUI());
        skills.addValueChangeListener(event -> updateUI());
        majorStudy.addValueChangeListener(event -> updateUI());
        degree.setValueChangeMode(ValueChangeMode.LAZY);
        degree.addValueChangeListener(event -> updateUI());

        continueButton.addClickListener(click -> {
            registrationService.registerStudent(firstname.getValue(), lastname.getValue(), skills.getValue(), majorStudy.getValue(), degree.getValue());
        });


        H2 header = new H2("Your student profile");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("custom-form-layout");
        verticalLayout.setHeight("700px");
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(
                header,
                formLayout,
                continueButton
        );

        verticalLayout.setHeight("");

        return verticalLayout;
    }

    private void updateUI() {
        if (registrationService.valideFieldsStudent(firstname.getValue(), lastname.getValue(), skills.getValue(), majorStudy.getValue(), degree.getValue())) {
            continueButton.setEnabled(true);
            continueButton.getStyle().set("color", "white");
        }
    }
}