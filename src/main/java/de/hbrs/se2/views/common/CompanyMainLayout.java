package de.hbrs.se2.views.common;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

@PageTitle("CompanyMain")
public class CompanyMainLayout extends AppLayout {


    private H2 viewTitle;

    public CompanyMainLayout() {
        setPrimarySection(AppLayout.Section.DRAWER);
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
        H1 appName = new H1("CompanyJobCafe");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        // Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, /*scroller,*/ createFooter());
    }

//        private SideNav createNavigation() {
//            SideNav nav = new SideNav();
//
//            nav.addItem(new SideNavItem("Profile", CompanyProfile.class, LineAwesomeIcon.USER.create()));
//            nav.addItem(new SideNavItem("Search", HomeView.class, LineAwesomeIcon.SEARCH_SOLID.create()));
//            nav.addItem(new SideNavItem("Advertissment", AboutView.class, LineAwesomeIcon.TASKS_SOLID.create()));
//            nav.addItem(new SideNavItem("Logout", JobFeedView.class, LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create()));
//
//            return nav;
//        }

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


