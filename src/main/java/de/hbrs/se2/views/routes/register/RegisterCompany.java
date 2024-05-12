package de.hbrs.se2.views.routes.register;


import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.Registration.RegistrationService;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.Headerlayout.HeaderLayout;

import java.io.InputStream;

@Route(value = Constant.Value.Route.REGISTERCOMPANY/*, registerAtStartup = false*/)
public class RegisterCompany extends Div {

    private final RegistrationService registrationService;
    private final Button continueButton = new Button("Continue");
    VerticalLayout registercompview;
    private TextField companyName;
    private TextField industry;
    private TextArea description;

    private InputStream inputStream;


    public RegisterCompany(RegistrationService registrationService) {
        getStyle().set("padding", "0");
        this.registrationService = registrationService;
        HeaderLayout headerLayout = new HeaderLayout();
        registercompview = createRegisterCompView(registrationService);
        add(headerLayout, registercompview);
    }


    public VerticalLayout createRegisterCompView(RegistrationService registrationService) {

        companyName = new TextField();
        companyName.setRequiredIndicatorVisible(true);

        industry = new TextField();
        industry.setRequiredIndicatorVisible(true);

        description = new TextArea();
        description.setWidth("400px");
        description.setHeight("250px");
        description.setRequiredIndicatorVisible(true);
        int charLimit = 10000;
        description.setMaxLength(charLimit);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimit));


        FormLayout formLayout = new FormLayout();
        formLayout.setWidth("572px");
        formLayout.getStyle().set("align-self", "center");
        formLayout.addComponentAsFirst(companyName);
        formLayout.addFormItem(companyName, "Company Name");
        formLayout.addFormItem(industry, "Industry");
        formLayout.addFormItem(description, "Description");


        // upload logo
        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addClassName("register-company-button-2");
        upload.addSucceededListener(event -> {
            inputStream = buffer.getInputStream();  // Mit dem ContinueButton wird das Logo der Company gesetzt
            updateUI();
        });


        continueButton.setEnabled(false);
        continueButton.addClassName("register-company-button-1");
        continueButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        continueButton.setMinWidth("150px");

        companyName.setValueChangeMode(ValueChangeMode.LAZY);   //Die Veränderung benötigt kein Klicken auf dem Bildschirm klar und der continuebutton wird blockiert/freigegeben
        companyName.addValueChangeListener(event -> updateUI());
        industry.setValueChangeMode(ValueChangeMode.LAZY);
        industry.addValueChangeListener(event -> updateUI());
        description.setValueChangeMode(ValueChangeMode.LAZY);
        description.addValueChangeListener(event -> updateUI());


        continueButton.addClickListener(click -> registrationService.registerCompany(companyName.getValue(), industry.getValue(), description.getValue(), inputStream));

        Label setProfilePic = new Label("Set your profile picture");
        setProfilePic.addClassName("setProfilePic");
        H2 header = new H2("Your company profile");
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.addClassName("custom-form-layout");
        verticalLayout.setHeight("700px");
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(
                header,
                formLayout,
                setProfilePic,
                upload,
                continueButton
        );
        return verticalLayout;
    }

    private void updateUI() {
        if (registrationService.validateFieldsCompany(companyName.getValue(), industry.getValue(), description.getValue())) {
            continueButton.setEnabled(true);
            continueButton.getStyle().set("color", "white");
        } else {
            continueButton.setEnabled(false);
        }
    }
}
