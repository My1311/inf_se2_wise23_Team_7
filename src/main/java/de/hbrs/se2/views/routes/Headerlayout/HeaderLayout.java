package de.hbrs.se2.views.routes.Headerlayout;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class HeaderLayout extends HorizontalLayout {

    public HeaderLayout(){

        H2 header = new H2("Coll@HBRS");
        header.getStyle().set("margin-top","1.5rem").set("margin-bottom","1.5rem");
        header.addClassName("landing-header-title");

        HorizontalLayout layout = new HorizontalLayout(header);
        layout.addClassName("landing-header-layout");
        layout.setWidthFull();
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        add(layout);
    }

}
