package de.hbrs.se2.views.routes.apply;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.apply.ApplyService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@PageTitle("Profile")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.EACHAPPLICANT, layout = LoggedInCompany.class)
public class EachApplicantView extends FormLayout {
    private final TextField first_name = new TextField("First Name");
    private final TextField last_name = new TextField("Last Name");
    private final TextField major_study = new TextField("Major Study");
    private final TextField list_of_skills = new TextField("List of skills");
    private final TextField degree = new TextField("Degree");
    private final TextArea cover_letter = new TextArea("Cover Letter");
    private final TextField date_of_application = new TextField("Date of Application");
    private final Button acceptButton = new Button("Accept");
    private final Button rejectButton = new Button("Reject");
    private final CompanyService companyService;
    private final ApplyService applyService;
    private final LoginService loginService;
    private User currentUser;
    private Company currentCompany;
    private Apply apply;

    @PostConstruct
    private void initPage() {
        this.currentUser = loginService.getUser();
        if (this.currentUser != null) {
            this.currentCompany = companyService.getCompanyByEmail(this.currentUser.getEmail());
        } else { // When there is not a logged-in user, then he/she has to go login.
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.LOGIN);
            Notification.show("Please Login!", 5, Notification.Position.TOP_CENTER);
            return;
        }
        this.apply = (Apply) UI.getCurrent().getSession().getAttribute(SessionAttributes.APPLY);
        createInfoOfStudent();

    }

    private void createInfoOfStudent() {
        VerticalLayout layoutColumn1 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutColumn1.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        layoutColumn1.setWidth("100%");
        layoutColumn1.setMaxWidth("800px");
        layoutColumn1.setHeight("min-content");
        h3.setText("Student Profile from " + this.apply.getStudent().getFirst_name() + " " + this.apply.getStudent().getLast_name());
        h3.setWidth("100%");
        h3.getStyle().set("align-self", "center");
        formLayout2Col.setWidth("100%");
        first_name.setWidth("50%");
        last_name.setWidth("50%");
        list_of_skills.setWidth("max-content");
        major_study.setWidth("50%");
        degree.setWidth("50%");
        cover_letter.setWidth("100%");
        cover_letter.setHeight("500px");
        date_of_application.setWidth("50%");
        layoutColumn1.setAlignSelf(FlexComponent.Alignment.CENTER, list_of_skills);
        layoutRow.addClassName(LumoUtility.Gap.LARGE);
        layoutRow.setWidth("100%");

        acceptButton.setWidth("min-content");
        acceptButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        acceptButton.addClassName("save-Button");
        this.acceptButton.addClickListener((event) -> acceptApplicant(event));
        this.rejectButton.addClickListener((event) -> rejectApplicant(event));
        rejectButton.setWidth("min-content");

        add(layoutColumn1);
        layoutColumn1.add(h3);
        layoutColumn1.add(formLayout2Col);
        formLayout2Col.add(first_name, last_name, list_of_skills, major_study, degree, cover_letter, date_of_application);
        formLayout2Col.setColspan(first_name, 1);
        formLayout2Col.setColspan(list_of_skills, 2);
        formLayout2Col.setColspan(major_study, 1);
        layoutColumn1.add(hr, layoutRow);
        layoutRow.add(acceptButton, rejectButton);

        this.first_name.setReadOnly(true);
        this.last_name.setReadOnly(true);
        this.degree.setReadOnly(true);
        this.major_study.setReadOnly(true);
        this.list_of_skills.setReadOnly(true);
        this.cover_letter.setReadOnly(true);
        this.date_of_application.setReadOnly(true);
        bindInputFieldsWithStudentData();
    }

    private void rejectApplicant(ClickEvent<Button> event) {
        try {
            this.applyService.rejectApplicant(this.apply);
            Notification.show("You rejected this applicant!", 5, Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate(ApplicantsListView.class);
        } catch (Exception e) {
            Notification.show("You already rejected this applicant!", 5, Notification.Position.TOP_CENTER);
        }
    }


    private void bindInputFieldsWithStudentData() {
        this.first_name.setValue(this.apply.getStudent().getFirst_name());
        this.last_name.setValue(this.apply.getStudent().getLast_name());
        this.list_of_skills.setValue(this.apply.getStudent().getList_of_skills());
        this.major_study.setValue(this.apply.getStudent().getMajor_study());
        this.degree.setValue(this.apply.getStudent().getDegree());
        this.cover_letter.setValue(this.apply.getText());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy").withZone(ZoneId.systemDefault());
        this.date_of_application.setValue(dateTimeFormatter.format(this.apply.getApplied_time()));
    }

    private void acceptApplicant(ClickEvent<Button> event) {
        try {
            this.applyService.acceptApplicant(this.apply);
            Notification.show("You accepted this applicant!", 10, Notification.Position.TOP_CENTER);
            UI.getCurrent().navigate(ApplicantsListView.class);
        } catch (Exception e) {
            Notification.show("You already accepted this applicant!", 10, Notification.Position.TOP_CENTER);
        }
    }
}
