package de.hbrs.se2.views.common;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * The main view is a top-level placeholder for other views.
 */
@PageTitle("Main")
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        //toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("StudentJobCafe");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        // Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, /*scroller,*/ createFooter());
    }

//    private SideNav createNavigation() {
//        SideNav nav = new SideNav();
//
//        nav.addItem(new SideNavItem("Profile", EditStudentProfile.class, LineAwesomeIcon.USER.create()));
//        nav.addItem(new SideNavItem("Home", HomeView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
//        nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.FILE.create()));
//        nav.addItem(new SideNavItem("JobFeed", JobFeedView.class, LineAwesomeIcon.LIST_SOLID.create()));
//        nav.addItem(new SideNavItem("CompanySearch", CompanySearchView.class, LineAwesomeIcon.LIST_SOLID.create()));
//
//        return nav;
//    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}
