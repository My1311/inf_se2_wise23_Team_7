package de.hbrs.se2.views.advertisement;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.shared.Registration;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;

public class AdvertisementForm extends FormLayout {
    // wenn die Componenten den gleichen Namen haben wie unser Entity, dann kann der Vaadin Binder zum automatischen Verbinden zw. den beiden verwendet werden

    private final TextField name = new TextField("name");
    private final NumberField PLZ = new NumberField("PLZ"); //
    private final TextField city = new TextField("city"); //
    private final TextField street = new TextField("street"); //
    private final NumberField hnumber = new NumberField("number"); //
    private final DatePicker deadline = new DatePicker("deadline");
    private final TextField requirements = new TextField("required skills");
    private final NumberField hourlywage = new NumberField("hourly wage");
    private final TextArea description = new TextArea("content");
    Button save = new Button("save");
    Button delete = new Button("delete");
    Button cancel = new Button("cancel");
    private final RadioButtonGroup<String> status = new RadioButtonGroup<>("status", "active", "inactive");
    private AdvertisementDTO advertisement;

    private final AdvertisementService advertisementService;
    private final LoginService loginService;
    private final String companyEmail;


    public AdvertisementForm(AdvertisementService advertisementService, LoginService loginService) {
        addClassName("advertisement-form");
        this.advertisementService = advertisementService;
        this.loginService = loginService;
        companyEmail = loginService.getUser().getEmail();

        add(
                getContent()
        );
    }

    static String[] extractAdress(AdvertisementDTO advertisementDTO) {  // Mit dieser Methode wird die Adresse aufgeteilt in PLZ / Stadt / Straße / Hausnummer
        return advertisementDTO.getOrt().split("/");    // advertisementDTO.getOrt() ist die komplette Adresse !
    }

    public void setAdvertisement(AdvertisementDTO advertisement) {
        if (advertisement != null) {
            setAdvertisementFields(advertisement, name, extractAdress(advertisement), PLZ, city, street, hnumber, deadline, requirements, hourlywage, description, status);
        }
        this.advertisement = advertisement;
    }

    private void setAdvertisementFields(AdvertisementDTO advertisement, TextField name, String[] adress, NumberField plz, TextField city, TextField street, NumberField hnumber, DatePicker deadline, TextField requirements, NumberField hourlywage, TextArea description, RadioButtonGroup<String> status) {
        name.setValue(advertisement.getName());
        plz.setValue(Double.parseDouble(adress[0]));
        city.setValue(adress[1]);
        street.setValue(adress[2]);
        hnumber.setValue(Double.parseDouble(adress[3]));
        deadline.setValue(advertisement.getDeadline());
        requirements.setValue(advertisement.getRequirements());
        hourlywage.setValue(advertisement.getHourlyWage());
        description.setValue(advertisement.getDescription());
        status.setValue(checkStatus(advertisement));
    }

    private String checkStatus(AdvertisementDTO advertisement) {
        if (advertisement.isActive()) {
            return "active";
        } else {
            return "inactive";
        }
    }

    private FormLayout getContent() {
        FormLayout formLayout = new FormLayout();

        //adress
        PLZ.setWidth("100px");
        street.setWidth("250px");
        city.setWidth("133px");
        hnumber.setWidth("75px");

        deadline.addClassName("deadline");

        hourlywage.setSuffixComponent(new Span("€"));
        hourlywage.addClassName("hourlywage");


        int charLimit = 10000;
        description.setMaxLength(charLimit);
        description.setValueChangeMode(ValueChangeMode.EAGER);
        description.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimit));
        description.setHeight("150px");
        HorizontalLayout horizontalLayout = new HorizontalLayout(PLZ, city);
        HorizontalLayout streetAndHnumberLayout = new HorizontalLayout(street, hnumber);
        HorizontalLayout deadlineAndWageLayout = new HorizontalLayout(deadline, hourlywage);
        formLayout.add(
                name,      //Name der Ausschreibung
                horizontalLayout,     // PLZ, Stadt
                streetAndHnumberLayout,   // Straße, Hausnummer
                deadlineAndWageLayout,        // Deadline und // Stundenlohn
                requirements,     // Anforderungen
                description,     // Beschreibung
                status,
                createButtonsLayout()
        );
        return formLayout;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, advertisement)));
        cancel.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        HorizontalLayout Buttons = new HorizontalLayout(save, delete, cancel);
        Buttons.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        return Buttons;
    }

    private void validateAndSave() {
        try {
            System.out.println("VALIDATEANDSAVE");
            updateAdvertisement(advertisement);
            fireEvent(new SaveEvent(this, advertisement));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void updateAdvertisement(AdvertisementDTO advertisement) {
        advertisementService.updateAdvertisementOfCompany(
                companyEmail,
                advertisement.getName(),
                description.getValue(),
                deadline.getValue(),
                hourlywage.getValue(),
                getAdress(),
                checkStatus(),
                requirements.getValue()
        );
    }

    private boolean checkStatus() {
        return status.getValue().equals("active");
    }

    private String getAdress() {
        return PLZ.getValue() + "/" + city.getValue() + "/" + street.getValue() + "/" + hnumber.getValue();
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    // Events   https://vaadin.com/docs/latest/tutorial/forms-and-validation
    public static abstract class AdvertisementFormEvent extends ComponentEvent<AdvertisementForm> {
        private final AdvertisementDTO advertisement;

        protected AdvertisementFormEvent(AdvertisementForm source, AdvertisementDTO advertisement) {
            super(source, false);
            this.advertisement = advertisement;
        }

        public AdvertisementDTO getAdvertisement() {
            return advertisement;
        }
    }

    public static class SaveEvent extends AdvertisementFormEvent {
        SaveEvent(AdvertisementForm source, AdvertisementDTO advertisement) {
            super(source, advertisement);
        }
    }

    public static class DeleteEvent extends AdvertisementFormEvent {
        DeleteEvent(AdvertisementForm source, AdvertisementDTO advertisement) {
            super(source, advertisement);
        }

    }

    public static class CloseEvent extends AdvertisementFormEvent {
        CloseEvent(AdvertisementForm source) {
            super(source, null);
        }
    }

//    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
//        return addListener(DeleteEvent.class, listener);
//    }
//
//    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
//        return addListener(SaveEvent.class, listener);
//    }
//    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
//        return addListener(CloseEvent.class, listener);
//    }

}

