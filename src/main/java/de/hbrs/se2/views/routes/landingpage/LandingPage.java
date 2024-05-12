package de.hbrs.se2.views.routes.landingpage;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.PageTitle;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.login.Login;
import de.hbrs.se2.views.routes.register.Register;

@Route(value = Constant.Value.Route.LANDINGPAGE)
@PageTitle("Coll@HBRS | Home")

public class LandingPage extends VerticalLayout {

    public LandingPage() {
        setSizeFull();
        getStyle().set("padding", "0");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        setAlignItems(Alignment.CENTER);

        // Header with title and login button
        H1 title = new H1("Coll@HBRS");
        title.getStyle().set("margin-top", "-1rem");
        title.addClassName("landing-header-title");

        H3 subtitle = new H3("Connecting students and companies");
        subtitle.getStyle().set("margin-top", "-2rem").set("margin-bottom", "1rem");
        subtitle.addClassName("landing-sub-header");

        VerticalLayout titleLayout = new VerticalLayout(title, subtitle);
        titleLayout.setWidthFull();
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.getStyle().set("margin-left", "20rem").set("margin-right", "10rem").set("margin-top", "1rem");

        Button loginButton = new Button("Login", e -> UI.getCurrent().navigate(Login.class));
        loginButton.getStyle().set("margin-right", "4rem");
        loginButton.addClassName("landing-login-button");
        loginButton.getStyle().set("border-radius", "15px").set("box-shadow", "0 8px 16px 0 rgba(0,0,0,0.3)");

        HorizontalLayout headerLayout = new HorizontalLayout(titleLayout, loginButton);
        headerLayout.addClassName("landing-header-layout");
        headerLayout.setWidthFull();
        headerLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        headerLayout.setAlignItems(Alignment.CENTER);

        Icon searchIcon = VaadinIcon.SEARCH.create();
        Icon applyIcon = VaadinIcon.CLIPBOARD_TEXT.create();
        Icon bellIcon = VaadinIcon.BELL.create();

        // Main content with search, apply, and chat sections
        HorizontalLayout searchSection = createSection("Search Filters", searchIcon, "Find companies that match your interests and skills.");
        HorizontalLayout applySection = createSection("Application", applyIcon, "Apply easily to interesting positions directly through the app and receive quick responses.");
        HorizontalLayout chatSection = createSection("Chat", bellIcon, "Communicate directly with companies and address all queries through our user-friendly chat function.");

        Image img = new Image("https://cdn.pixabay.com/photo/2019/03/28/10/22/idea-4086855_1280.jpg", "MyLogo");
        img.setWidth("60%");
        img.setHeight("60%");
        Image img2 = new Image("https://cdn.pixabay.com/photo/2017/12/13/18/05/networks-3017398_1280.jpg", "MyLogo2");
        img2.setWidth("65%");
        img2.setHeight("65%");

        VerticalLayout searchSectionV = new VerticalLayout(img, searchSection);
        searchSectionV.setAlignItems(Alignment.CENTER);
        searchSectionV.setJustifyContentMode(JustifyContentMode.CENTER);
        VerticalLayout chatSectionV = new VerticalLayout(img2, chatSection);
        chatSectionV.setAlignItems(Alignment.CENTER);
        chatSectionV.setJustifyContentMode(JustifyContentMode.CENTER);

        H2 headerApply = new H2("Why Coll@HBRS?");
        headerApply.getStyle().set("margin-top", "1rem").set("margin-bottom", "1rem");
        VerticalLayout applySectionV = new VerticalLayout(headerApply, applySection);
        applySectionV.setAlignItems(Alignment.CENTER);
        applySectionV.setJustifyContentMode(JustifyContentMode.CENTER);

        HorizontalLayout mainContent = new HorizontalLayout(searchSectionV, applySectionV, chatSectionV);
        mainContent.setWidthFull();
        mainContent.setJustifyContentMode(JustifyContentMode.EVENLY);

        // Footer with registration button
        Button registerButton = new Button("Register for free here!", e -> UI.getCurrent().navigate(Register.class));
        registerButton.getStyle().set("margin-top", "-5rem").set("border-radius", "10px");
        registerButton.setClassName("landing-register-button");

        add(headerLayout, mainContent, registerButton);
    }

    private HorizontalLayout createSection(String title, Icon icon, String description) {
        H3 header = new H3(title);
        Span descriptionLabel = new Span(description);
        descriptionLabel.addClassName("landing-centered-text");
        HorizontalLayout headerLayout = new HorizontalLayout(icon, header);
        headerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        headerLayout.setAlignItems(Alignment.CENTER);
        icon.getStyle().set("margin-top", "20px");

        VerticalLayout sectionLayout = new VerticalLayout(headerLayout, descriptionLabel);
        sectionLayout.setAlignItems(Alignment.CENTER);
        sectionLayout.setSpacing(false);
        sectionLayout.setSizeFull();

        HorizontalLayout layout = new HorizontalLayout(sectionLayout);
        layout.setSizeFull();
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        return layout;
    }
}
