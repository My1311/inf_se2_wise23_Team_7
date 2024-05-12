package de.hbrs.se2.views.routes.profile;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.routes.loggedin.LoggedInStudent;

@Route(value = "login/Companyname_Studentlayout/profile"/*,registerAtStartup = false*/, layout = LoggedInStudent.class)
public class CompanyProfile_Studentlayout extends VerticalLayout {

    private final CompanyService companyService;
    private final StudentService studentService;

    private final TextField companyNameField;
    private final TextField industryField;
    private final TextArea descriptionField;

    public CompanyProfile_Studentlayout(CompanyService companyService, StudentService studentService) {

        this.companyService = companyService;
        this.studentService = studentService;

        setSpacing(true);


        Image placeholderImage = new Image("https://cdn.pixabay.com/photo/2017/11/10/05/24/add-2935429_1280.png", "Placeholder Image");
        placeholderImage.setWidth("200px");
        placeholderImage.setHeight("200px");
        placeholderImage.getStyle().set("border-radius", "50%");

        add(placeholderImage);

        companyNameField = new TextField("Company Name");
        companyNameField.setValue(companyService.getCompanyByEmail(SessionAttributes.getCurrentUser().getEmail()).getName());

        industryField = new TextField("Industry");
        industryField.setValue(companyService.getCompanyByEmail(SessionAttributes.getCurrentUser().getEmail()).getIndustry());

        descriptionField = new TextArea("Description");
        descriptionField.setValue(companyService.getCompanyByEmail(SessionAttributes.getCurrentUser().getEmail()).getDescription());


        descriptionField.setWidth("440px");
        descriptionField.setHeight("250px");


        descriptionField.setHeight("300px");


        setAlignItems(Alignment.CENTER);

        VerticalLayout fieldlayout1 = new VerticalLayout(companyNameField,
                industryField);
        HorizontalLayout fieldlayout2 = new HorizontalLayout(placeholderImage, fieldlayout1);
        VerticalLayout fieldLayout = new VerticalLayout(
                fieldlayout2, descriptionField
        );

        Button saveButton = new Button("Save");
        saveButton.addClickListener(event -> onSaveButtonClick());

        Button discardButton = new Button("Discard");
        discardButton.addClickListener(event -> onDiscardButtonClick());
        HorizontalLayout formLayout = new HorizontalLayout(saveButton, discardButton);
        formLayout.setAlignItems(Alignment.END);
        formLayout.add(saveButton, discardButton);


        add(fieldLayout, formLayout);
    }

    private void onSaveButtonClick() {
        Notification.show("Changes Saved!");
    }

    private void onDiscardButtonClick() {
        Notification.show("Changes discarded!");
    }

    public void setValues(String companyNameField, String industryField, String descriptionField) {
        this.companyNameField.setValue(companyNameField);
        this.industryField.setValue(industryField);
        this.descriptionField.setValue(descriptionField);
    }

}
