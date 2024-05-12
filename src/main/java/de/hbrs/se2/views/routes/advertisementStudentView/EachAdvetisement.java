package de.hbrs.se2.views.routes.advertisementStudentView;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.apply.ApplyService;
import de.hbrs.se2.control.rating.RatingService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.rating.Rating;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.common.StudentMainLayout;
import de.hbrs.se2.views.components.RatingGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.time.Instant;

@PageTitle("Advertisement Number")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.EACHADVERTISEMENT, layout = StudentMainLayout.class)
public class EachAdvetisement extends FormLayout {
    private final TextField name = new TextField("Titel");
    private final TextField company_name = new TextField("Company Name");
    private final TextField isActive = new TextField("isActive");
    private final TextField ort = new TextField("Adresse");
    // private final TextField jobType = new TextField("JobType");
    private final TextField deadline = new TextField("Dealine");
    private final TextArea requirements = new TextArea("Required Skills");
    private final TextField hourly_wage = new TextField("Hourly Wage");
    private final TextArea description = new TextArea("Description");
    private final Button applyButton = new Button("Apply");
    private final Button backButton = new Button("Back to All Advertisement");
    private final Button cancelButton = new Button("Cancel this Apply");
    private final TextArea applyFieldText = new TextArea("Letter of application");

    private final Button sendButton = new Button("Send");
    private final H3 confirmation = new H3();
    private final Button sendRatingButton = new Button("Send evaluation");
    private final TextField evaluationText = new TextField("Evaluation Text");
    private final H3 pleaseWriteh3 = new H3("Please write your letter of application!");

    private final HorizontalLayout layoutRow = new HorizontalLayout();
    private final ApplyService applyService;
    private final StudentService studentService;
    private final LoginService loginService;
    private final RatingService ratingService;
    public VerticalLayout layoutColumn2 = new VerticalLayout();
    private final RatingGenerator ratingGenerator = new RatingGenerator();
    private User currentUser;
    private Student currentStudent;
    private Advertisement advertisement;
    private Apply apply;
    // For Student View
    private Rating rating;

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
        this.advertisement = (Advertisement) UI.getCurrent().getSession().getAttribute(SessionAttributes.ADVERTISEMENT);
        createInfoOfAdvertisement();

    }

    private void createInfoOfAdvertisement() {
        bindInputFieldsWithAdvertisementData();
        VerticalLayout layoutColumn = new VerticalLayout();
        H3 h3 = new H3();
        HorizontalLayout layoutRow = new HorizontalLayout();
        Hr hr = new Hr();
        h3.setText(this.advertisement.getName());
        h3.getStyle().set("align-self", "center");
        layoutColumn.add(h3, company_name, isActive, ort, deadline, requirements, hourly_wage, description);
        layoutRow.add(applyButton, backButton, cancelButton);
        layoutColumn.add(hr, layoutRow);
        add(layoutColumn);
        layoutColumn.setWidth("100%");
        layoutRow.setWidth("100%");
        this.company_name.setReadOnly(true);
        this.isActive.setReadOnly(true);
        this.ort.setReadOnly(true);
        this.deadline.setReadOnly(true);
        this.requirements.setReadOnly(true);
        this.hourly_wage.setReadOnly(true);
        this.description.setReadOnly(true);

        this.company_name.setWidth("100%");
        this.isActive.setWidth("100%");
        this.ort.setWidth("100%");
        this.deadline.setWidth("100%");
        this.requirements.setWidth("100%");
        this.hourly_wage.setWidth("25%");
        this.description.setWidth("100%");
        this.description.setHeight("35%");

        applyButton.setWidth("min-content");
        applyButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        applyButton.addClassName("apply-Button");
        this.applyButton.addClickListener(event -> showApplyApplication());
        backButton.setWidth("min-content");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.backButton.addClickListener((event) -> {
            UI.getCurrent().getPage().setLocation(Constant.Value.Route.ALLADVERTISEMENT);
        });
        cancelButton.setWidth("min-content");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        this.cancelButton.addClickListener((buttonClickEvent -> deleteApply()));
    }

    private void deleteApply() {
        if (this.apply != null) {
            this.applyService.delete(this.apply);
            Notification.show("Your application is now canceled.");
        } else {
            Notification.show("You have not applied for this job yet.");
        }
    }

    private void bindInputFieldsWithAdvertisementData() {
        this.name.setValue(this.advertisement.getName());
        this.company_name.setValue(this.advertisement.getCompany().getName());
        this.isActive.setValue(this.advertisement.isActive() ? "Active" : "Inactive");
        this.ort.setValue(this.advertisement.getOrt());
        this.deadline.setValue(this.advertisement.getDeadline().toString());
        this.requirements.setValue(this.advertisement.getRequirements());
        this.hourly_wage.setValue(this.advertisement.getHourlyWage().toString());
        this.description.setValue(this.advertisement.getDescription());
    }

    private void showApplyApplication() {
        layoutColumn2.removeAll();

        layoutColumn2.setWidthFull();
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("1000px");
        layoutColumn2.add(pleaseWriteh3);
        layoutColumn2.add(applyFieldText);
        layoutRow.add(sendButton);
        layoutColumn2.add(layoutRow);
        applyFieldText.setRequiredIndicatorVisible(true);
        this.applyFieldText.setWidth("100%");
        int charLimit = 10000;
        applyFieldText.setMaxLength(charLimit);
        applyFieldText.setValueChangeMode(ValueChangeMode.EAGER);
        applyFieldText.addValueChangeListener(e -> e.getSource().setHelperText(e.getValue().length() + "/" + charLimit));
        applyFieldText.setHeight("150px");

        sendButton.setWidth("min-content");
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendButton.addClickListener(buttonClickEvent -> sendListener());

        confirmation.setText("You haven't applied for this job yet.");
        layoutColumn2.add(confirmation);

        add(layoutColumn2);
        writeApplyLetter();

    }


    private void writeApplyLetter() {
        if (this.currentStudent != null) {
            this.apply = this.applyService.getApplyByStudentAndAdvertisement(this.currentStudent, this.advertisement);
            if (this.apply != null) {
                // Student darf nicht mehr um diese Stelle bewerben
                this.applyFieldText.setValue(this.apply.getText());
                this.applyFieldText.setReadOnly(true);

                this.sendButton.setVisible(false);
                showConfirmation();
                showRating();

            } else {
                // Student kann um diese Stelle bewerben
                this.sendButton.setVisible(true);

            }
        }
    }

    private boolean requiredFieldIsFilled() {
        if (this.applyFieldText.getValue().isEmpty()) {
            Notification.show("Letter of Application is required!");
            return false;
        }
        return true;
    }

    private void sendListener() {
        //Todo: Chat benachrichtigung
        if (requiredFieldIsFilled()) {
            this.apply = Apply.builder()
                    .text(this.applyFieldText.getValue())
                    .student(this.currentStudent)
                    .applied_time(Instant.now())
                    .advertisement(this.advertisement).build();
            Notification.show("Your application letter is now sent.");
            this.applyService.insert(this.apply);
            this.applyFieldText.setReadOnly(true);
            // applyButton.addThemeVariants(ButtonVariant.); change the color of sth
            remove(sendButton);
            // Fall: erst bewerben, dann bewerbung abschicken (Send), the text wird nicht gleich verandert, sonder mann muss
            // noch mal die Seite loaden
            confirmation.setText("You have already applied for this job.");
        }
    }

    private void showConfirmation() {
        confirmation.setText("You have already applied for this job.");
    }

    private void showRating() {
        this.rating = this.ratingService.findRatingByStudentAndCompany(this.currentStudent, this.advertisement.getCompany());
        if (this.rating != null) {
            showRatedStars();
        }
        else {
            H3 h3 = new H3();
            h3.setText("Can you please take a moment to rate this company?"); // funktioniert bis hier
            HorizontalLayout layoutRow = new HorizontalLayout();
            layoutRow.add(sendRatingButton);
            this.evaluationText.setWidth("100%");
            this.evaluationText.setPlaceholder("Please write your evaluation here.");

            sendRatingButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            sendRatingButton.addClickListener(event -> sendRatingListener());

            layoutColumn2.add(h3, ratingGenerator.getRatingButton()
                    , evaluationText, layoutRow);
            writeEvaluationText();
        }
    }

    private void writeEvaluationText() {
        if (this.currentStudent != null) {
            this.rating = this.ratingService.findRatingByStudentAndCompany(this.currentStudent, this.advertisement.getCompany());
            if (this.rating != null) {
                // Student darf nicht mehr um diese Stelle bewerben
                // this.rating.setValue(this.rating.getValue());
                this.evaluationText.setReadOnly(true);
                this.sendButton.setVisible(false);
            } else {
                // Student kann diese Firma bewerten
                // this.rating.setValue(ratingGenerator.getCurrentRatingIndex());
                this.evaluationText.getValue();
                this.sendButton.setVisible(true);
            }
        }
    }

    private void sendRatingListener() {
        this.rating = Rating.builder()
                .timestamp(Instant.now())
                .value(ratingGenerator.getCurrentRatingIndex() + 1)
                .text(this.evaluationText.getValue())
                .student(this.currentStudent)
                .company(this.advertisement.getCompany()).build();
        Notification.show("Your evaluation is now sent.");
        this.ratingService.addRating(this.rating);
        this.sendRatingButton.setVisible(false);
        showRatedStars();
    }

    private Component showRatedStars() {
        this.rating = this.ratingService.findRatingByStudentAndCompany(this.currentStudent, this.advertisement.getCompany());
        if (this.rating != null) {
            this.evaluationText.setValue(this.rating.getText());
            Component stars = ratingGenerator.getRatingDisplay(this.rating.getValue());
            layoutColumn2.add(stars);
            H3 h3 = new H3();
            h3.setText("Thank you for your feedback!");
            layoutColumn2.add(evaluationText,h3);
            this.evaluationText.setReadOnly(true);
        }
        return layoutColumn2;
    }

}
