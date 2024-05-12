package de.hbrs.se2.views.common;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.views.advertisement.AdvertisementSearchView;
import de.hbrs.se2.views.routes.advertisementStudentView.AllAdvertisementView;
import de.hbrs.se2.views.routes.companySearch.CompanySearchView;
import de.hbrs.se2.views.routes.loggedin.LoggedInStudent;
import de.hbrs.se2.views.routes.login.Login;
import org.aspectj.weaver.ast.Not;

import javax.annotation.PostConstruct;


@PageTitle("StudentMain")
public class StudentMainLayout extends AppLayout {

    @PostConstruct
    private void init() {
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
        Button logoutButton = new Button("Log out", VaadinIcon.SIGN_OUT.create());
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

        //Elements
        RouterLink profile = new RouterLink("Profile", LoggedInStudent.class);
        RouterLink search = new RouterLink("Search", CompanySearchView.class);
/*
        RouterLink applications = new RouterLink(" Applications", null); // aufgabe für sprint 2

*/
        RouterLink advertisenments = new RouterLink("Advertisenments", AdvertisementSearchView.class);


        RouterLink logout = new RouterLink("Log out", Login.class);
        RouterLink advertisement2 = new RouterLink("AdvertisementApply", AllAdvertisementView.class);

        // Icons hinzufügen
        Icon profileIcon = VaadinIcon.USER.create();
        Icon searchIcon = VaadinIcon.SEARCH.create();
        Icon advertisenmentsIcon = VaadinIcon.BOOK.create();
        Icon advertisenmentsIcon2 = VaadinIcon.BOOK.create();
        Icon logoutIcon = VaadinIcon.CLOSE.create();

        HorizontalLayout hl1 = new HorizontalLayout(profileIcon, profile);
        HorizontalLayout hl2 = new HorizontalLayout(searchIcon, search);
        HorizontalLayout hl3 = new HorizontalLayout(advertisenmentsIcon, advertisenments);
//        HorizontalLayout hl4 = new HorizontalLayout(logoutIcon, logout);
        HorizontalLayout hl4 = new HorizontalLayout(advertisenmentsIcon2, advertisement2);
        //Set
        profile.setHighlightCondition(HighlightConditions.sameLocation());
        search.setHighlightCondition(HighlightConditions.sameLocation());
        advertisenments.setHighlightCondition(HighlightConditions.sameLocation());
        logout.setHighlightCondition(HighlightConditions.sameLocation());
        advertisement2.setHighlightCondition(HighlightConditions.sameLocation());

        //Style
        profile.getStyle().set("color", "black");
        search.getStyle().set("color", "black");
        advertisenments.getStyle().set("color", "black");
        logout.getStyle().set("color", "black");
        profileIcon.getStyle().set("color", "grey");
        searchIcon.getStyle().set("color", "grey");
        advertisenmentsIcon.getStyle().set("color", "grey");
        logoutIcon.getStyle().set("color", "grey");
        advertisement2.getStyle().set("color", "black");


        addToDrawer(new VerticalLayout(
                hl1,
                hl2,
                hl3,
                hl4
        ));
    }

}
