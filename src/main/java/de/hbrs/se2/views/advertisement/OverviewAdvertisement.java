package de.hbrs.se2.views.advertisement;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.util.ArrayList;
import java.util.List;

@Lazy
@Route(value = Constant.Value.Route.COMPANYADVERTISEMENTS, layout = LoggedInCompany.class)
//@CssImport(value = "./themes/styles.css", themeFor = "vaadin-grid")   // funktioniert irgendwie nicht
// toDO: Grid-Selector-Farbe auf Blau stellen
public class OverviewAdvertisement extends VerticalLayout {

    private final LoginService loginService;
    private final UserService userService;
    AdvertisementService advertisementService;
    List<AdvertisementDTO> Advertisements;

    Grid<AdvertisementDTO> grid = new Grid<>();
    TextField filterText = new TextField();
    AdvertisementForm advertisementForm;


    @Autowired
    public OverviewAdvertisement(LoginService loginService, AdvertisementService advertisementService, UserService userService) {
        addClassName("OverviewAdvertisement");
        setSizeFull();
        this.loginService = loginService;
        this.userService = userService;

        Advertisements = new ArrayList<>();
        this.advertisementService = advertisementService;
        this.Advertisements = advertisementService.getAllAddsOfCompany(loginService.getUser().getEmail());

        configureGrid();
        configureForm();

        individualView();

        closeEditor();
    }

    private void closeEditor() {
        advertisementForm.setAdvertisement(null);
        advertisementForm.setVisible(false);
        removeClassName("editing");
    }


    private void individualView() {
        if (userService.identifyRole(loginService.getUser()) instanceof Student) {
            add(createheader(), createAdvertisementButton());
        } else {
            add(createheader(),
//                    getToolbar(),     // wegen fehlender Funktion erstmal entfernt
                    getContent(),
                    createAdvertisementButton()
            );
        }
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, advertisementForm);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, advertisementForm);
        content.addClassName("content");
        content.setSizeFull();

        return content;
    }

    private void configureForm() {
        advertisementForm = new AdvertisementForm(advertisementService, loginService);
        advertisementForm.setWidth("25em");

        advertisementForm.addListener(AdvertisementForm.SaveEvent.class, this::saveAdvertisement);
        advertisementForm.addListener(AdvertisementForm.DeleteEvent.class, this::deleteAdvertisement);
        advertisementForm.addListener(AdvertisementForm.CloseEvent.class, event -> closeEditor());
    }

    private void deleteAdvertisement(AdvertisementForm.DeleteEvent event) {
        advertisementService.deleteAdvertisement(loginService.getUser().getEmail(), event.getAdvertisement().getName());
        updateList();
        closeEditor();
    }

    private void saveAdvertisement(AdvertisementForm.SaveEvent event) {
        AdvertisementDTO advertisement = event.getAdvertisement();
//        advertisementService.
//        advertisementService.updateAdvertisementOfCompany(
//                                                            loginService.getUser().getEmail(),
//                                                            advertisement.getName(),
//                                                            advertisement.getDescription(),
//                                                            advertisement.getDeadline(),
//                                                            advertisement.getHourlyWage(),
//                                                            advertisement.getOrt(),
//                                                            advertisement.isActive(),
//                                                            advertisement.getRequirements()
//                                                        );
        updateList();
        closeEditor();
    }


    private void configureGrid() {
        grid.addClassName("advertisement-grid");
        grid.setSizeFull();
        ListDataProvider<AdvertisementDTO> dataProvider = new ListDataProvider<>(Advertisements);
        grid.setDataProvider(dataProvider);
        grid.addColumn(AdvertisementDTO::getName).setHeader("Title");
        grid.addColumn(AdvertisementDTO::getHourlyWage).setHeader("Hourly wage");
        grid.addColumn(AdvertisementDTO::getRequirements).setHeader("Requirements");
        grid.addColumn(AdvertisementDTO::getDeadline).setHeader("Deadline");
        grid.addColumn(this::semanticStatus).setHeader("Status");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editContact(event.getValue()));   // wenn jemand ein Advertisement auswählt
    }

    private String semanticStatus(AdvertisementDTO advertisementDTO) {
        return advertisementDTO.isActive() ? "active" : "inactive";
    }

    private void editContact(AdvertisementDTO advertisementDTO) {
        if (advertisementDTO == null) {   // wenn jemand das Advertisement deselected
            closeEditor();
        } else {
            advertisementForm.setAdvertisement(advertisementDTO);
            advertisementForm.setVisible(true);
            addClassName("editing");
        }
    }

    private Component getToolbar() {    // Toolbar für evtl. später
        filterText.setPlaceholder("Filter by name");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);        // warte bis der User aufhört zu tippen
        filterText.addValueChangeListener(event -> updateList());
        return filterText;
    }

    private void updateList() { // funktioniert mit gegebener funktion leider noch nicht, es muss eine andere implementiert werden
//        grid.setItems(advertisementService.getAdvertisementDTOOfCompanyByName(loginService.getUser().getEmail(),filterText.getValue()));
        grid.setItems(advertisementService.getAllAddsOfCompany(loginService.getUser().getEmail()));
    }

    private void getAdvertisementDTO() {
        Advertisements.addAll(advertisementService.getAllAddsOfCompany(loginService.getUser().getEmail()));
    }

    public VerticalLayout createheader() {
        VerticalLayout verticalLayout = new VerticalLayout();
        H2 header = new H2("Advertisements");
        header.addClassName("Advertisements-Header");
        verticalLayout.add(header);
        verticalLayout.setWidthFull();
        // weitere Verschiebung nach unten
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return verticalLayout;
    }

    private VerticalLayout createAdvertisementButton() {
        var createButton = new Button("create advertisement");
        //<theme-editor-local-classname>
        createButton.addClassName("overview-advertisement-button-1");
        createButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createButton.addClickListener(click -> UI.getCurrent().navigate(CreateAdvertisement.class));

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.add(
                createButton
        );
        createButton.addClickShortcut(Key.ENTER);

        // Positionierung
        verticalLayout.setWidthFull();
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        return verticalLayout;
    }


}
