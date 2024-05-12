package de.hbrs.se2.views.advertisement;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;


@Route(value = Constant.Value.Route.COMPANYCREATEADVERTISEMENT, layout = LoggedInCompany.class)
public class CreateAdvertisement extends Div {

    private final LoginService loginService;
    private final TextField title = new TextField("title");
    //address
    private final IntegerField PLZ = new IntegerField("PLZ");
    private final TextField city = new TextField("city");
    private final TextField street = new TextField("street");
    private final IntegerField hnumber = new IntegerField("number");
    private final DatePicker deadline = new DatePicker("deadline");
    private final TextField skills = new TextField("required skills");
    private final NumberField hourlywage = new NumberField("hourly wage");
    VerticalLayout createAdvertisementview;
    @Autowired
    CompanyService companyService;
    @Autowired
    AdvertisementService advertisementService;

    public CreateAdvertisement(LoginService loginService) {
        this.loginService = loginService;
        createAdvertisementview = createAdvertisementview();
        add(createAdvertisementview);
    }

    private VerticalLayout createAdvertisementview() {
        FormLayout formLayout = new FormLayout();

        PLZ.setWidth("100px");
        street.setWidth("250px");
        city.setWidth("133px");
        hnumber.setWidth("75px");

        deadline.addClassName("deadline");

        hourlywage.setSuffixComponent(new Span("€"));
        hourlywage.addClassName("hourlywage");

        TextArea content = new TextArea("content");
        int charLimit = 10000;
        content.setMaxLength(charLimit);
        content.setValueChangeMode(ValueChangeMode.EAGER);
        content.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimit));
        content.setWidth("400px");
        content.setHeight("350px");


        // Felder werden notwendig gemacht.
        title.setRequiredIndicatorVisible(true);
        PLZ.setRequiredIndicatorVisible(true);
        city.setRequiredIndicatorVisible(true);
        street.setRequiredIndicatorVisible(true);
        hnumber.setRequiredIndicatorVisible(true);
        deadline.setRequiredIndicatorVisible(true);
        skills.setRequiredIndicatorVisible(true);
        hourlywage.setRequiredIndicatorVisible(true);
        content.setRequiredIndicatorVisible(true);


        HorizontalLayout horizontalLayout = new HorizontalLayout(); // Adresse in einer Zeile zusammenfassen
        horizontalLayout.add(
                PLZ,
                city,
                street,
                hnumber
        );


        formLayout.add(
                title,      //Name der Ausschreibung
                horizontalLayout,     // Ort (PLZ, Ort, Straße und Hausnummer
                deadline,        // Zeit
                skills,     // Anforderungen
                hourlywage, // Stundenlohn
                content     // Beschreibung
        );

        // Zentrierung
        formLayout.setWidth("572px");
        formLayout.getStyle().set("align-self", "center");

        var createButton = new Button("create");
        var cancelButton = new Button("cancel");
        //<theme-editor-local-classname>
        createButton.addClassName("overview-advertisement-button-1");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        createButton.addClickShortcut(Key.ENTER);
        cancelButton.addClickShortcut(Key.ESCAPE);
        createButton.addClassName("create-Button");
        cancelButton.addClassName("cancel-Button");
        createButton.addClickListener(click -> {
            if (!(deadline.getValue() instanceof LocalDate)) {    // Exception wenn Text in deadline eingefügt und gesaved wird. Danach geht save nicht mehr.
                Notification.show("Please choose a correct date as a deadline!", 5000, Notification.Position.BOTTOM_START);
            } else {
                String address = getAddress();
                String companyEmail = loginService.getUser().getEmail();
                advertisementService.addAdvertisement(companyEmail, title.getValue(), content.getValue(), deadline.getValue(), hourlywage.getValue(), address, true, skills.getValue());
                UI.getCurrent().navigate(OverviewAdvertisement.class);
            }
        });

        cancelButton.addClickListener(click -> UI.getCurrent().navigate(OverviewAdvertisement.class));
        HorizontalLayout horButtonLayout = new HorizontalLayout(cancelButton, createButton);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(
                formLayout,
                horButtonLayout
        );

//        verticalLayout.setWidth("400px");     //WICHTIG: Wenn man ein Formlayout in einem Verticallayout hat und die Width von Verticallayout festlegt, gibt es keine Zentrierung mehr.

        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        return verticalLayout;
    }

    private String getAddress() { // Adresse aus den Eingabefeldern zusammenfassen
        return PLZ.getValue() + "/" + city.getValue() + "/" + street.getValue() + "/" + hnumber.getValue();
    }
}
