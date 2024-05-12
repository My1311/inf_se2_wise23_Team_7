package de.hbrs.se2.views.routes.profile;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.model.company.CompanyDTO;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
@Route(value = "CompanyForStudents",layout = LoggedInCompany.class)
public class CompanyForStudents extends VerticalLayout {
    private TextField companyNameField = new TextField("Company Name");
    private TextField industryField = new TextField("Industry");
    private TextArea descriptionField = new TextArea("Description");

    public CompanyForStudents() {
        setSpacing(true);

        Image placeholderImage = new Image("https://cdn.pixabay.com/photo/2017/11/10/05/24/add-2935429_1280.png", "Placeholder Image");
        placeholderImage.setWidth("200px");
        placeholderImage.setHeight("200px");
        placeholderImage.getStyle().set("border-radius", "50%");

        add(placeholderImage);

        this.companyNameField.setReadOnly(true);
        this.industryField.setReadOnly(true);
        this.descriptionField.setReadOnly(true);

        this.descriptionField.setWidth("440px");
        this.descriptionField.setHeight("300px");

        setAlignItems(Alignment.CENTER);

        VerticalLayout fieldlayout1 = new VerticalLayout(companyNameField, industryField);
        HorizontalLayout fieldlayout2 = new HorizontalLayout(placeholderImage, fieldlayout1);
        VerticalLayout fieldLayout = new VerticalLayout(fieldlayout2,descriptionField);

        add(fieldLayout);
    }

    public void setValues(CompanyDTO companyDTO) {
        this.companyNameField.setValue(companyDTO.getName());
        this.industryField.setValue(companyDTO.getIndustry());
        this.descriptionField.setValue(companyDTO.getDescription());
    }
}
