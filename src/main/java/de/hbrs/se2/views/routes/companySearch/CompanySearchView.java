package de.hbrs.se2.views.routes.companySearch;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyDTO;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.util.Constant;
import com.vaadin.flow.component.textfield.TextField;
import de.hbrs.se2.views.common.StudentMainLayout;
import de.hbrs.se2.views.routes.loggedin.LoggedInCompany;
import de.hbrs.se2.views.routes.loggedin.LoggedInStudent;
import de.hbrs.se2.views.routes.profile.CompanyForStudents;
import de.hbrs.se2.views.routes.profile.CompanyProfile;
import de.hbrs.se2.views.routes.profile.CompanyProfile_Studentlayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


@PageTitle("Company Search")
//@Route(value = "companySearch", layout = MainLayout.class, registerAtStartup = false)
@Route(value = Constant.Value.Route.COMPANYSEARCH, layout = StudentMainLayout.class)
public class CompanySearchView extends Div {
    private List<CompanyDTO> companyList;
    private final CompanyService companyService;

    private Iterator<List<CompanyDTO>> companyDTOIterator;
    private String saveSearchCompany = null;
    private String saveSearchIndustry = null;
    private CompanyForStudents companyForStudents;
    
    public CompanySearchView(CompanyService companyService, CompanyForStudents companyForStudents) {
        companyDTOIterator = companyService.doPaging(10, saveSearchCompany, saveSearchIndustry);

        companyList = companyDTOIterator.next();
        this.companyForStudents = companyForStudents;

        this.companyService = companyService;

        add(this.createTitle());
        add(this.createTable());
    }

    private Component createTable() {
        Grid<CompanyDTO> grid = new Grid<>();

        ListDataProvider <CompanyDTO> dataProvider = new ListDataProvider<>(companyList);

        grid.setDataProvider(dataProvider);

        Grid.Column<CompanyDTO> name = grid.addColumn(CompanyDTO::getName).setHeader("Name");
        Grid.Column<CompanyDTO> logo = grid.addColumn(CompanyDTO::getLogo).setHeader("Logo");
        Grid.Column<CompanyDTO> industry = grid.addColumn(CompanyDTO::getIndustry).setHeader("Industry");
        Grid.Column<CompanyDTO> description = grid.addColumn(CompanyDTO::getDescription).setHeader("Description");
        Grid.Column<CompanyDTO> email = grid.addColumn(CompanyDTO::getEmail).setHeader("Email");

        Grid.Column<CompanyDTO> profile = grid.addComponentColumn(companyDTO -> {
            Button profileButton = new Button("View Profile");
            profileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            profileButton.addClickListener(event -> showCompanyDetails(companyDTO));
            return profileButton;
        }).setHeader("Profile");


        HeaderRow filterRow = grid.appendHeaderRow();
        TextField nameSearch = new TextField();
        nameSearch.addValueChangeListener(event ->  {
            dataProvider.getItems().clear();
            saveSearchCompany = nameSearch.getValue().equals("") ? null: nameSearch.getValue();
            companyDTOIterator = companyService.doPaging(10, saveSearchCompany, saveSearchIndustry);

            List<CompanyDTO> filtered = companyDTOIterator.next();

            if(filtered  != null) {
                for (CompanyDTO companyDTO : filtered) {
                    dataProvider.getItems().add(companyDTO);
                }
            }
            grid.getDataProvider().refreshAll();
        });

        nameSearch.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(name).setComponent(nameSearch);
        nameSearch.setSizeFull();
        nameSearch.setPlaceholder("Filter");

        Button loadMoreButton = new Button("Load More");
        loadMoreButton.addClickListener(event -> {
             List<CompanyDTO> next = companyDTOIterator.next();

             if(next != null) {
                 for (CompanyDTO companyDTO : next) {
                     dataProvider.getItems().add(companyDTO);
                 }
             }
             grid.getDataProvider().refreshAll();
        });

        FooterRow loadMoreFooter = grid.appendFooterRow();
        loadMoreFooter.getCell(industry).setComponent(loadMoreButton);

        TextField industrySearch = new TextField();
        industrySearch.addValueChangeListener(event ->  {
            dataProvider.getItems().clear();
            saveSearchIndustry= industrySearch.getValue().equals("") ? null: industrySearch.getValue();
            companyDTOIterator = companyService.doPaging(10, saveSearchCompany, saveSearchIndustry);

            List<CompanyDTO> filtered = companyDTOIterator.next();

            if(filtered  != null) {
                for (CompanyDTO companyDTO : filtered) {
                    dataProvider.getItems().add(companyDTO);
                }
            }
            grid.getDataProvider().refreshAll();
        });

        industrySearch.setValueChangeMode(ValueChangeMode.EAGER);

        filterRow.getCell(industry).setComponent(industrySearch);
        industrySearch.setSizeFull();
        industrySearch.setPlaceholder("Filter");

        return grid;
    }

    private Component createTitle() {
        return new H3("Search for Companies");
    }

    private void showCompanyDetails(CompanyDTO companyDTO) {

        try {
            this.companyForStudents.setValues(companyDTO);
            UI.getCurrent().navigate(CompanyForStudents.class);
        } catch (NoSuchElementException e) {
            Notification.show("Company doesnt exist");
        }
    }
}
