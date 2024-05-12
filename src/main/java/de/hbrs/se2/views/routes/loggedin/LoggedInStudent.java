package de.hbrs.se2.views.routes.loggedin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.common.StudentMainLayout;
import de.hbrs.se2.views.routes.companySearch.CompanySearchView;
import de.hbrs.se2.views.routes.login.Login;
import de.hbrs.se2.views.routes.profile.EditStudentProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;


@PageTitle("My Profile")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.LOGGEDINSTUDENT, layout = StudentMainLayout.class)
public class LoggedInStudent extends AppLayout {
    private final TextField first_name = new TextField("First Name");
    private final TextField last_name = new TextField("Last Name");
    private final TextField major_study = new TextField("Major Study");
    private final TextArea list_of_skills = new TextArea("List of Skills");
    private final TextField degree = new TextField("Degree");
    private final Button edit = new Button("Edit");

    private final StudentService studentService;
    private final LoginService userService;
    private final LoginService loginService;
    private User currentUser;
    private Student currentStudent;

    @PostConstruct
    public void init() {
        this.currentUser = loginService.getUser();
        if (this.currentUser != null) {
            this.currentStudent = studentService.findStudentByUser(this.currentUser);
        } else { // When there is not a logged-in user, then he/she has to go login.
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.LOGIN);
            Notification.show("Please Login!", 5, Notification.Position.TOP_CENTER);
            return;
        }
        createInfoOfStudent();
    }

    private void createInfoOfStudent() {
        bindInputFieldsWithStudentData();
        edit.addClickListener((event) -> {
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.EDITPROFILE);
        });
        /**
         * Name
         */
        VerticalLayout layoutColumn1 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        Hr hr = new Hr();
        HorizontalLayout layoutRow = new HorizontalLayout();

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
        setContent(layoutColumn1);
        layoutColumn1.add(h3, edit);
        layoutColumn1.add(formLayout2Col);
        formLayout2Col.add(first_name, last_name, list_of_skills, major_study, degree);
        formLayout2Col.setColspan(first_name, 1);
        formLayout2Col.setColspan(list_of_skills, 2);
        formLayout2Col.setColspan(major_study, 1);
        layoutColumn1.add(hr, layoutRow);

        /**
         * reset the input fields to their values from db
         */

        this.first_name.setReadOnly(true);
        this.last_name.setReadOnly(true);
        this.degree.setReadOnly(true);
        this.major_study.setReadOnly(true);
        this.list_of_skills.setReadOnly(true);
    }

    private void bindInputFieldsWithStudentData() {
        if (this.currentUser != null) {
            if (this.currentStudent != null) {
                this.first_name.setValue(this.currentStudent.getFirst_name());
                this.last_name.setValue(this.currentStudent.getLast_name());
                this.list_of_skills.setValue(this.currentStudent.getList_of_skills());
                this.major_study.setValue(this.currentStudent.getMajor_study());
                this.degree.setValue(this.currentStudent.getDegree());
            }
        } else {
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.LOGIN);
            setContent(new Div(new H3("Please login before creating your Profile!")));
        }
    }

    private void studentData() {

    }

    private void createHeader() {

        //Komponenten
        H2 logo = new H2("Coll@HBRS");
        DrawerToggle drawerToggle = new DrawerToggle();
        HorizontalLayout header = new HorizontalLayout(drawerToggle, logo);

        //Positionierung
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();

        //Styles
        logo.addClassNames("text-s", "m-s", "logo");
        drawerToggle.addClassName("drawer_toggle");
        header.addClassNames("header_background");

        //Implementierung
        addToNavbar(header);

    }

    private void createDrawer() {

        //Elements
        RouterLink profile = new RouterLink("Profile", EditStudentProfile.class);
        RouterLink search = new RouterLink("Search", CompanySearchView.class);
/*
        RouterLink applications = new RouterLink(" Applications", null); // aufgabe für sprint 2
*/
        RouterLink logout = new RouterLink("Log out", Login.class);

        // Icons hinzufügen
        Icon profileIcon = VaadinIcon.USER.create();
        Icon searchIcon = VaadinIcon.SEARCH.create();
        Icon applicationsIcon = VaadinIcon.BOOK.create();
        Icon logoutIcon = VaadinIcon.CLOSE.create();

        HorizontalLayout hl1 = new HorizontalLayout(profileIcon, profile);
        HorizontalLayout hl2 = new HorizontalLayout(searchIcon, search);
        // HorizontalLayout hl3 = new HorizontalLayout(applicationsIcon, applications);
        HorizontalLayout hl4 = new HorizontalLayout(logoutIcon, logout);

        //Set
        profile.setHighlightCondition(HighlightConditions.sameLocation());
        search.setHighlightCondition(HighlightConditions.sameLocation());
        // applications.setHighlightCondition(HighlightConditions.sameLocation());
        logout.setHighlightCondition(HighlightConditions.sameLocation());

        //Style
        profile.getStyle().set("color", "black");
        search.getStyle().set("color", "black");
        // applications.getStyle().set("color", "black");
        logout.getStyle().set("color", "black");
        profileIcon.getStyle().set("color", "grey");
        searchIcon.getStyle().set("color", "grey");
        applicationsIcon.getStyle().set("color", "grey");
        logoutIcon.getStyle().set("color", "grey");


        addToDrawer(new VerticalLayout(
                hl1,
                hl2,
                // hl3,
                hl4
        ));
    }


}


