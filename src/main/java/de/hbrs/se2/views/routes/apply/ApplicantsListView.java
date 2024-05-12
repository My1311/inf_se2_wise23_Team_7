package de.hbrs.se2.views.routes.apply;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.apply.ApplyService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.student.StudentApplyDto;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;
import java.util.List;

@PageTitle("List of applicants")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.APPLICANTSlIST, layout = LoggedInCompany.class)
@Lazy
public class ApplicantsListView extends Div {
    // For Company View
    private final LoginService loginService;
    private final StudentService studentService;
    private final AdvertisementService advertisementService;
    private final ApplyService applyService;
    private final CompanyService companyService;
    List<Advertisement> advertisements;

    @PostConstruct
    public void init() {
        this.advertisements = advertisementService.getAllAdvertisementOfCompany(companyService.getCompanyByEmail(loginService.getUser().getEmail()));
        add(createCard());
    }

    public Component createCard() {
        VerticalLayout allCard = new VerticalLayout();
        for (int i = 0; i < advertisements.size(); i++) {
            allCard.add(createTitle(i));
            allCard.add(createTable(i));
        }
        return allCard;
    }

    private Component createTable(int i) {
        Grid<StudentApplyDto> grid = new Grid<>(StudentApplyDto.class, false);
        grid.addColumn(studentApplyDto -> studentApplyDto.getStudent().getFirst_name() + " " + studentApplyDto.getStudent().getLast_name()).setHeader("Name");
        grid.addColumn(studentApplyDto -> studentApplyDto.getStudent().getList_of_skills()).setHeader("List_of_skills");
        grid.addColumn(studentApplyDto -> studentApplyDto.getStudent().getMajor_study()).setHeader("Major study");
        grid.addColumn(studentApplyDto -> studentApplyDto.getStudent().getDegree()).setHeader("Degree");
        grid.addColumn(studentApplyDto -> studentApplyDto.getApplicationState().toString()).setHeader("Application State");

        grid.addComponentColumn(applicant -> {
            Button profileButton = new Button("View Full of Applicant");
            profileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            profileButton.addClickListener(event -> showStudentDetails(applicant, this.advertisements.get(i)));
            return profileButton;
        }).setHeader("Profile");
        List<StudentApplyDto> studentstList = this.studentService.findAllofApplicantsforAdvert(this.advertisements.get(i));
        ListDataProvider<StudentApplyDto> dataProvider = new ListDataProvider<>(studentstList);
        grid.setItems(dataProvider);
        VerticalLayout table = new VerticalLayout();
        table.setSizeFull();
        table.add(grid);
        return table;
    }

    private void showStudentDetails(StudentApplyDto applicant, Advertisement advertisement) {
        UI.getCurrent().getSession().setAttribute(SessionAttributes.APPLY, applyService.getApplyByStudentAndAdvertisement(applicant.getStudent(), advertisement));
        UI.getCurrent().navigate(EachApplicantView.class);
    }

    private Component createTitle(int i) {
        return new H3("For this Advertisement " + advertisements.get(i).getName());
    }


}
