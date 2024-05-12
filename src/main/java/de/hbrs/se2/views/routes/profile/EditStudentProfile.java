package de.hbrs.se2.views.routes.profile;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.common.StudentMainLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

@PageTitle("Profile")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.EDITPROFILE, layout = StudentMainLayout.class)
public class EditStudentProfile extends FormLayout {

    private final TextField first_name = new TextField("First Name");
    private final TextField last_name = new TextField("Last Name");
    private final TextField major_study = new TextField("Major Study");
    private final TextField list_of_skills = new TextField("List of skills");
    private final TextField degree = new TextField("Degree");

    private final Button saveButton = new Button("Save");
    private final Button backButton = new Button("Back to My Profile");
    private final Button resetButton = new Button("Reset");
    private final StudentService studentService;
    private final LoginService userService;
    private final LoginService loginService;
    private User currentUser;
    private Student currentStudent;

    @PostConstruct
    private void initPage() {
        this.currentUser = loginService.getUser();
        if (this.currentUser != null) {
            this.currentStudent = studentService.findStudentByUser(this.currentUser);
        } else { // When there is not a logged-in user, then he/she has to go login.
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.LOGIN);
            Notification.show("Please Login!", 5, Notification.Position.TOP_CENTER);
            return;
        }

        VerticalLayout layoutColumn1 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();
        setWidth("100%");
        getStyle().set("align-self", "center");
        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);

        layoutRow.setAlignSelf(FlexComponent.Alignment.CENTER, layoutRow);
        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        layoutColumn1.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        layoutColumn1.setWidth("100%");
        layoutColumn1.setMaxWidth("800px");
        layoutColumn1.setHeight("min-content");
        h3.setText("Student Profile from " + this.currentStudent.getFirst_name() + " " + this.currentStudent.getLast_name());
        h3.setWidth("100%");
        h3.getStyle().set("align-self", "center");
        formLayout2Col.setWidth("100%");
        first_name.setWidth("50%");
        last_name.setWidth("50%");
        list_of_skills.setWidth("max-content");
        major_study.setWidth("50%");
        degree.setWidth("50%");
        layoutColumn1.setAlignSelf(FlexComponent.Alignment.CENTER, list_of_skills);
        layoutRow.addClassName(LumoUtility.Gap.LARGE);
        layoutRow.setWidth("100%");
        // layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClassName("save-Button");
        this.saveButton.addClickListener((event) -> saveListener(event));
        backButton.setWidth("min-content");
        resetButton.setWidth("min-content");
        add(layoutColumn1);
        layoutColumn1.add(h3);
        layoutColumn1.add(formLayout2Col);
        formLayout2Col.add(first_name, last_name, list_of_skills, major_study, degree);
        formLayout2Col.setColspan(first_name, 1);
        formLayout2Col.setColspan(list_of_skills, 2);
        formLayout2Col.setColspan(major_study, 1);
        layoutColumn1.add(hr, layoutRow);
        layoutRow.add(saveButton, resetButton, backButton);

        first_name.setRequiredIndicatorVisible(true);
        last_name.setRequiredIndicatorVisible(true);
        major_study.setRequiredIndicatorVisible(true);

        /**
         * reset the input fields to their values from db
         */
        backButton.setIcon(VaadinIcon.BACKSPACE.create());
        saveButton.setIcon(VaadinIcon.SAFE.create());
        //saveButton.addClickListener()
        backButton.addClickListener((event) -> {
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.LOGGEDINSTUDENT);
        });
        resetButton.addClickListener(buttonClickEvent -> bindInputFieldsWithStudentData());
        bindInputFieldsWithStudentData();
    }

    private boolean requiredFieldsAreFilled() {
        if (this.first_name.getValue().isEmpty()) {
            Notification.show("First name is required!");
            return false;
        }
        if (this.last_name.getValue().isEmpty()) {
            Notification.show("Last name is required!");
            return false;
        }
        if (this.major_study.getValue().isEmpty()) {
            Notification.show("Major Study is required!");
            return false;
        }
        return true;
    }


    private void saveListener(ClickEvent<Button> event) {
        if (requiredFieldsAreFilled()) {
            if (this.currentStudent != null) { // Wenn der benutzer ein student schon erstellt hat
                this.currentStudent.setDegree(this.degree.getValue());
                this.currentStudent.setFirst_name(this.first_name.getValue());
                this.currentStudent.setLast_name(this.last_name.getValue());
                this.currentStudent.setMajor_study(this.major_study.getValue());
                this.currentStudent.setList_of_skills(this.list_of_skills.getValue());

                this.studentService.addStudent(this.currentStudent);
                Notification.show("Student updated");
            } else {
                Student student = Student.builder()
                        .degree(this.degree.getValue())
                        .first_name(this.first_name.getValue())
                        .last_name(this.last_name.getValue())
                        .major_study(this.major_study.getValue())
                        .list_of_skills(this.list_of_skills.getValue())
                        .user(this.currentUser).build();

                this.userService.addUser(this.currentUser);
                this.studentService.addStudent(student);
                Notification.show("Student added");
            }
        }
    }


    private void bindInputFieldsWithStudentData() {
        if (this.currentStudent != null) {
            this.first_name.setValue(this.currentStudent.getFirst_name());
            this.last_name.setValue(this.currentStudent.getLast_name());
            this.list_of_skills.setValue(this.currentStudent.getList_of_skills());
            this.major_study.setValue(this.currentStudent.getMajor_study());
            this.degree.setValue(this.currentStudent.getDegree());
        }
    }
}
