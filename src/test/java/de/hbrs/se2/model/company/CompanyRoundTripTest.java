package de.hbrs.se2.model.company;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.user.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CompanyRoundTripTest {

    @Autowired
    private LoginService userService;
    @Autowired
    private CompanyService companyService;
    private Company company;

    @BeforeEach
    void setup() {
        company = DefaultValues.DEFAULT_COMPANY;
    }

    @Test
    @Order(1)
    @DisplayName("Company wird erstellt und in DB hinzugefügt.")
    void testInsertCompany() {
        User dbUser = this.userService.addUser(this.company.getUser());
        assertEquals(this.company.getUser(), dbUser);
        Company dbCompany = this.companyService.addCompany(this.company);
        assertNotNull(company);
        assertEquals(company, dbCompany);
        assertEquals(company.getLogo(), dbCompany.getLogo());
        assertEquals(company.getName(), dbCompany.getName());
        assertEquals(company.getIndustry(), dbCompany.getIndustry());
        assertEquals(company.getDescription(), dbCompany.getDescription());
        assertEquals(company.getPhoneNumber(), dbCompany.getPhoneNumber());
    }

    @Order(2)
    @Test
    @DisplayName("Company wird aus der DB gelesen.")
    void testReadCompany() {
        Company dbCompany = this.companyService.getCompanyByEmail(this.company.getUser().getEmail());
        assertNotNull(dbCompany);
        assertEquals(this.company, dbCompany);
    }


    @Order(3)
    @Test
    @DisplayName("Company wird in der DB updated.")
    void testUpdateCompany() {
        this.company.setIndustry("TestUpdateTestUpdate");
        this.companyService.addCompany(this.company);
        Company dbCompany = this.companyService.getCompanyByEmail(this.company.getUser().getEmail());
        assertNotNull(dbCompany);
        assertEquals(this.company.getIndustry(), dbCompany.getIndustry());
    }

    @Test
    @Order(4)
    @DisplayName("Company wird aus der DB gelöscht.")
    void testDeleteCompany() {
        this.companyService.deleteCompany(company);
        Company dbCompany = this.companyService.getCompanyByEmail(this.company.getUser().getEmail());
        assertNull(dbCompany);
        this.userService.delete(company.getUser());
        User dbUser = this.userService.findUserByEmail(this.company.getUser().getEmail());
        assertNull(dbUser);
    }

    @Test
    @Order(5)
    @DisplayName("Company wird durch gegebene Email gefunden können oder nicht.")
    void testGetCompanyByEmail() {
        User dbUser = this.userService.addUser(this.company.getUser());
        Company dbCompany = this.companyService.addCompany(this.company);
        assert dbUser != null;
        Company foundCompany = this.companyService.getCompanyByEmail(dbUser.getEmail());
        assertNotNull(foundCompany);
        assertEquals(foundCompany, dbCompany);
        this.companyService.deleteCompany(company);
        this.userService.delete(company.getUser());
    }
}
