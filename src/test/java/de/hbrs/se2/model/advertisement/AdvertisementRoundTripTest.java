package de.hbrs.se2.model.advertisement;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.user.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AdvertisementRoundTripTest {

    private final Advertisement advertisement = DefaultValues.DEFAULT_ADVERTISEMENT;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private LoginService userService;
    // Methode getAllOfCompany wird mit Frontend Tests getestet werden.
    // Test 1,2,3,5 laufen gut.

    @Order(1)
    @Test
    @DisplayName("Advertisement wird in die DB hinzufueget.")
    void testInsertAdvertisement() {// Passed
        User dbUser = this.userService.addUser(this.advertisement.getCompany().getUser());
        assertNotNull(dbUser);
        assertEquals(this.advertisement.getCompany().getUser(), dbUser);

        Company dbCompany = this.companyService.createCompany(this.advertisement.getCompany());
        assertNotNull(dbCompany);
        assertEquals(this.advertisement.getCompany(), dbCompany);
        Advertisement dbAdvertisement = this.advertisementService.insert(this.advertisement);
        assertNotNull(dbAdvertisement);
        assertEquals(this.advertisement, dbAdvertisement);
    }

    @Order(2)
    @Test
    @DisplayName("Advertisement wird aus der DB gelesen.")
    void testReadAdvertisement() {
        Advertisement dbAdvertisement = this.advertisementService.findAdvertisementById(this.advertisement.getId());
        assertNotNull(dbAdvertisement);
        assertEquals(this.advertisement, dbAdvertisement);
    }

    // ADVERTISEMENT SERVICE TESTS
    @Order(3)
    @Test
    @DisplayName("Set Methode benutzen, Entitat testen: Advertisement wird in der DB updated.")
    void testUpdateAdvertisement() {
        this.advertisement.setDescription("TestUpdate");
        this.advertisementService.insert(advertisement);
        Advertisement dbAdvertisement = this.advertisementService.findAdvertisementById(advertisement.getId());
        assertNotNull(dbAdvertisement);
        assertEquals(this.advertisement.getDescription(), dbAdvertisement.getDescription());
    }

    @Order(4)
    @Test
    @DisplayName("Update Methode von Service testen. WICHTIG: My schreibt diese Methode NICHT.")
    void testUpdateAdvertisement2() {
        // Diese Methode speichert Company anstatt Advertisement.
        // Update Advertisement
        this.advertisementService.updateAdvertisementOfCompany(
                                                                this.advertisement.getCompany().getUser().getEmail(),
                                                                "Test1",
                                                                "Test2",
                                                                this.advertisement.getDeadline(),
                                                                15.00,
                                                                this.advertisement.getOrt(),
                                                                this.advertisement.isActive(),
                                                                this.advertisement.getRequirements()
                );
        // Find diese Advertisement mit ihrer ID
        Advertisement dbAdvertisement = this.advertisementService.findAdvertisementById(advertisement.getId());
        // Vergleichen
        assertEquals(this.advertisement.getDescription(), dbAdvertisement.getDescription());
    }


    @Order(5)
    @Test
    @DisplayName("Advertisement wird aus der DB geloscht.")
    void testDeleteAdvertisement() {
        this.advertisementService.delete(this.advertisement);
        Advertisement dbAdvertisement = this.advertisementService.findAdvertisementById(this.advertisement.getId());
        assertNull(dbAdvertisement);

        this.companyService.deleteCompany(this.advertisement.getCompany());
        assertThrows(NoSuchElementException.class, () -> this.companyService.getCompanyByEmail(this.advertisement.getCompany().getUser().getEmail()));

        this.userService.delete(this.advertisement.getCompany().getUser());
        User dbUser = this.userService.findUserByEmail(this.advertisement.getCompany().getUser().getEmail());
        assertNull(dbUser);
    }


}
