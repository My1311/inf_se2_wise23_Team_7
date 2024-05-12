package de.hbrs.se2.views.routes.loggedin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.advertisement.OverviewAdvertisement;
import de.hbrs.se2.views.routes.apply.ApplicantsListView;
import de.hbrs.se2.views.routes.companySearch.CompanySearchView;
import de.hbrs.se2.views.routes.login.Login;
import de.hbrs.se2.views.routes.profile.CompanyProfile;
import org.springframework.context.annotation.Lazy;

import java.util.Arrays;

@Lazy
@Route(value = Constant.Value.Route.LOGGEDINCOMPANY)
public class LoggedInCompany extends AppLayout {

    private final UserService userService;
    private final LoginService loginService;

    public LoggedInCompany(UserService userService, LoginService loginService) {
        this.userService = userService;
        this.loginService = loginService;
        createHeader();
        createDrawer();
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


        //logoutButton
        Button logoutButton = new Button("Logout", VaadinIcon.SIGN_OUT.create());
        logoutButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_SMALL);
        logoutButton.addClickListener(event -> UI.getCurrent().navigate(Login.class));

        logoutButton.addClassName("logoutButton");
        header.add(logoutButton);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.END);

        header.add(logoutButton);

        //Styles
        logo.addClassNames("text-s", "m-s", "logo");
        drawerToggle.addClassName("drawer_toggle");
        header.addClassNames("header_background");

        //Implementierung
        addToNavbar(header);

    }

    private void createDrawer() {

        //Elemente
        RouterLink profile = new RouterLink("Profile", CompanyProfile.class);
        RouterLink advertisements = new RouterLink(" Advertisements", OverviewAdvertisement.class);
        RouterLink applicants = new RouterLink("Applicants", ApplicantsListView.class);
        RouterLink backToSearch = new RouterLink("Back", CompanySearchView.class);


        // Icons hinzufügen
        Icon profileIcon = VaadinIcon.USER.create();
        Icon searchIcon = VaadinIcon.SEARCH.create();
        Icon advertisementsIcon = VaadinIcon.BOOK.create();
        Icon applicantsIcon = VaadinIcon.ALARM.create();
        Icon logoutIcon = VaadinIcon.CLOSE.create();


        //Layouts
        HorizontalLayout hl1 = new HorizontalLayout(profileIcon, profile);
        HorizontalLayout hl3 = new HorizontalLayout(advertisementsIcon, advertisements);
        HorizontalLayout hl6 = new HorizontalLayout(applicantsIcon, applicants);
        HorizontalLayout hl5 = new HorizontalLayout(logoutIcon, backToSearch);


        for (RouterLink routerLink : Arrays.asList(profile, advertisements, applicants, backToSearch)) {
            routerLink.setHighlightCondition(HighlightConditions.sameLocation());
            routerLink.getStyle().set("color", "black");
        }
        for (Icon icon : Arrays.asList(profileIcon, searchIcon, advertisementsIcon, applicantsIcon, logoutIcon)) {
            icon.getStyle().set("color", "grey");
        }


        if (userService.identifyRole(loginService.getUser()) instanceof Student) {
            addToDrawer(
                    new VerticalLayout(
                            hl1,
                            hl3,
                            hl5
                    )
            );
        } else {
            addToDrawer(new VerticalLayout(
                    hl1,
                    hl3,
                    hl6
            ));
        }

    }

}
