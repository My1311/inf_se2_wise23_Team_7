package de.hbrs.se2.views.routes.advertisementStudentView;

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
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.util.Constant;
import de.hbrs.se2.util.SessionAttributes;
import de.hbrs.se2.views.common.StudentMainLayout;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@PageTitle("All Advertisement")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Route(value = Constant.Value.Route.ALLADVERTISEMENT, layout = StudentMainLayout.class)
public class AllAdvertisementView extends Div {
    // For Student View

    private final AdvertisementService advertisementService;
    private final Grid<Advertisement> grid = new Grid<>(Advertisement.class, false);

    @PostConstruct
    public void init() {
        add(createTitle());
        add(createTable());
    }

    private Component createTable() {

        grid.addColumn(Advertisement::getName).setHeader("Title");
        grid.addColumn(advertisement -> advertisement.getCompany().getName()).setHeader("Company");
        grid.addColumn(Advertisement::getRequirements).setHeader("Requirements");
        grid.addColumn(Advertisement::getDeadline).setHeader("Deadline");
        grid.addComponentColumn(advert -> {
            Button profileButton = new Button("View Full of Advertisement");
            profileButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            profileButton.addClickListener(event -> showAdvertisementDetails(advert));
            return profileButton;
        }).setHeader("Profile");
        List<Advertisement> advertisementList = this.advertisementService.findAll();
        ListDataProvider<Advertisement> dataProvider = new ListDataProvider<>(advertisementList);
        grid.setItems(dataProvider);
        VerticalLayout table = new VerticalLayout();
        table.setSizeFull();
        table.add(grid);
        return table;
    }

    private Component createTitle() {
        H3 title = new H3("All of Advertisement");
        title.getStyle().set("text-align", "center");
        return title;
    }

    private void showAdvertisementDetails(Advertisement advertisement) {
        UI.getCurrent().getSession().setAttribute(SessionAttributes.ADVERTISEMENT, advertisement);
        UI.getCurrent().navigate(EachAdvetisement.class);
    }
}