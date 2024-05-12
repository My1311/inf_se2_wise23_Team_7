package de.hbrs.se2.views.advertisement;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.FooterRow;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.model.company.CompanyDTO;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.views.common.StudentMainLayout;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import com.vaadin.flow.component.textfield.TextField;

import java.util.Iterator;
import java.util.List;

@PageTitle("Advertisement Search")
//@Route(value = "companySearch", layout = MainLayout.class, registerAtStartup = false)
@Route(value = Constant.Value.Route.ADVERTISEMENTSEARCH, layout = StudentMainLayout.class)
public class AdvertisementSearchView extends Div {
    private final ApplicationContext applicationContext;
    private final AdvertisementService advertisementService;
    private List<AdvertisementDTO> advertisementList;
    private Iterator<List<AdvertisementDTO>> advertisementDTOIterator;

    private String saveSearchCompany = null;
    private String saveSearchIndustry = null;

    public AdvertisementSearchView(AdvertisementService advertisementService, ApplicationContext applicationContext) {
        this.advertisementService = advertisementService;
        advertisementDTOIterator = advertisementService.doPaging(10, saveSearchCompany, saveSearchIndustry);

        this.advertisementList = advertisementDTOIterator.next();
        this.applicationContext = applicationContext;

        add(this.crateTitle());
        add(this.createTable());
    }

    private Component createTable() {
        Grid<AdvertisementDTO> grid = new Grid<>();

        ListDataProvider<AdvertisementDTO> dataProvider = new ListDataProvider<>(advertisementList);
        grid.setDataProvider(dataProvider);

        Grid.Column<AdvertisementDTO> companyName = grid.addColumn(advertisementDTO -> advertisementDTO.getCompanyDTO().getName()).setHeader("Company Name");
        Grid.Column<AdvertisementDTO> companyIndustry = grid.addColumn(advertisementDTO -> advertisementDTO.getCompanyDTO().getIndustry()).setHeader("Company Industry");
        Grid.Column<AdvertisementDTO> companyMail = grid.addColumn(advertisementDTO -> advertisementDTO.getCompanyDTO().getEmail()).setHeader("Company Mail");
        Grid.Column<AdvertisementDTO> description = grid.addColumn(AdvertisementDTO::getDescription).setHeader("Description");
        Grid.Column<AdvertisementDTO> hourlyWage = grid.addColumn(AdvertisementDTO::getHourlyWage).setHeader("Hourly Wage");
        Grid.Column<AdvertisementDTO> location = grid.addColumn(AdvertisementDTO::getOrt).setHeader("Location");
        Grid.Column<AdvertisementDTO> requirements = grid.addColumn(AdvertisementDTO::getRequirements).setHeader("Requirements");
        Grid.Column<AdvertisementDTO> deadline = grid.addColumn(AdvertisementDTO::getDeadline).setHeader("Deadline");

        HeaderRow filterRow = grid.appendHeaderRow();
        TextField companyNameSearch = new TextField();
        companyNameSearch.addValueChangeListener(event ->  {
            dataProvider.getItems().clear();
            saveSearchCompany = companyNameSearch.getValue().equals("") ? null: companyNameSearch.getValue();
            advertisementDTOIterator = advertisementService.doPaging(10, saveSearchCompany, saveSearchIndustry);

            List<AdvertisementDTO> filtered = advertisementDTOIterator.next();

            if(filtered  != null) {
                for (AdvertisementDTO advertisementDTO : filtered) {
                    dataProvider.getItems().add(advertisementDTO);
                }
            }
            grid.getDataProvider().refreshAll();
        });

        TextField companyIndustrySearch = new TextField();
        companyIndustrySearch.addValueChangeListener(event ->  {
            dataProvider.getItems().clear();
            saveSearchIndustry = companyIndustrySearch.getValue().equals("") ? null: companyIndustrySearch.getValue();
            advertisementDTOIterator = advertisementService.doPaging(10, saveSearchCompany, saveSearchIndustry);

            List<AdvertisementDTO> filtered = advertisementDTOIterator.next();

            if(filtered  != null) {
                for (AdvertisementDTO advertisementDTO : filtered) {
                    dataProvider.getItems().add(advertisementDTO);
                }
            }
            grid.getDataProvider().refreshAll();
        });

        companyNameSearch.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(companyName).setComponent(companyNameSearch);
        companyNameSearch.setSizeFull();
        companyNameSearch.setPlaceholder("Filter");

        companyIndustrySearch.setValueChangeMode(ValueChangeMode.EAGER);
        filterRow.getCell(companyIndustry).setComponent(companyIndustrySearch);
        companyIndustrySearch.setSizeFull();
        companyIndustrySearch.setPlaceholder("Filter");

        Button loadMoreButton = new Button("Load More");
        loadMoreButton.addClickListener(event -> {
            List<AdvertisementDTO> next = advertisementDTOIterator.next();

            if(next != null) {
                for (AdvertisementDTO advertisementDTO : next) {
                    dataProvider.getItems().add(advertisementDTO);
                    grid.getDataProvider().refreshAll();
                }
            }
        });

        FooterRow loadMoreFooter = grid.appendFooterRow();
        loadMoreFooter.getCell(description).setComponent(loadMoreButton);

        return grid;
    }

    private Component crateTitle() {
        return new H3("Search for Advertisements");
    }

}
