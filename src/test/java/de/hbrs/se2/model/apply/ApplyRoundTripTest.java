package de.hbrs.se2.model.apply;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.apply.ApplyService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.student.Student;
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
public class ApplyRoundTripTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ApplyService applyService;
    @Autowired
    private AdvertisementService advertisementService;
    @Autowired
    private CompanyService companyService;
    private Apply apply;

    @BeforeEach
    void setup() {

        apply = DefaultValues.DEFAULT_APPLY;
    }

    @Test
    @Order(1)
    @DisplayName("Applicat  ion wird erstellt und in DB hinzugefugt.")
    void testInsertApplication() {
        this.loginService.addUser(this.apply.getStudent().getUser());
        this.loginService.addUser(this.apply.getAdvertisement().getCompany().getUser());
        studentService.addStudent(this.apply.getStudent());
        companyService.createCompany(this.apply.getAdvertisement().getCompany());
        advertisementService.insert(this.apply.getAdvertisement());


        Apply dbApply = this.applyService.insert(this.apply);
        assertNotNull(dbApply);
        assertEquals(this.apply, dbApply);
    }
    @Order(2)
    @Test
    @DisplayName("Application wird aus der DB gelesen.")
    void testReadApplication() {
//        Apply dbApply= this.applyService.getApplyByStudentAndAdvertisement(this.apply.getStudent(), this.apply.getAdvertisement());
//        assertNotNull(dbApply);
//        assertEquals(this.apply, dbApply);
        Student student = this.apply.getStudent();
        Advertisement advertisement = this.apply.getAdvertisement();
        System.out.println("Student ID: " + student.getLast_name());
        System.out.println("Advertisement ID: " + advertisement.getName());

        Apply dbApply = this.applyService.getApplyByStudentAndAdvertisement(student, advertisement);
        assertNotNull(dbApply);
        assertEquals(this.apply.getId(), dbApply.getId());
        //assertEquals(this.apply, dbApply); geht nicht, weil assertEquals die equals nur die applyService Objekte, die andres sind vergleicht.
    }

    @Order(3)
    @Test
    @DisplayName("Application wird in der DB updated.")
    void testUpdateApplication() {
        this.apply.setText("TestUpdateTestUpdate");
        this.applyService.insert(this.apply);
        Apply dbApply = this.applyService.getApplyByStudentAndAdvertisement(this.apply.getStudent(), this.apply.getAdvertisement());
        assertNotNull(dbApply);
        assertEquals(this.apply.getText(), dbApply.getText());
    }
    @Order(4)
    @Test
    @DisplayName("The state of Application wird getestet.")
    void testAcceptApplication() {
        this.applyService.acceptApplicant(this.apply);
        Apply dbApply = this.applyService.getApplyByStudentAndAdvertisement(this.apply.getStudent(), this.apply.getAdvertisement());
        assertNotNull(dbApply);
        assertEquals(this.apply.getState(), dbApply.getState());
    }
    @Order(5)
    @Test
    @DisplayName("The state of Application wird getestet.")

    void testRejectApplication() {
        this.applyService.rejectApplicant(this.apply);
        Apply dbApply = this.applyService.getApplyByStudentAndAdvertisement(this.apply.getStudent(), this.apply.getAdvertisement());
        assertNotNull(dbApply);
        assertEquals(this.apply.getState(), dbApply.getState());
    }

    @Test
    @Order(6)
    @DisplayName("Application wird aus der DB geloscht.")
    void testDeleteApplication() {
        this.applyService.delete(apply);
        Apply dbApply = this.applyService.getApplyByStudentAndAdvertisement(this.apply.getStudent(), this.apply.getAdvertisement());
        assertNull(dbApply);
        studentService.delete(this.apply.getStudent());
        this.loginService.delete(this.apply.getStudent().getUser());
        advertisementService.delete(this.apply.getAdvertisement());
        companyService.deleteCompany(this.apply.getAdvertisement().getCompany());
        this.loginService.delete(this.apply.getAdvertisement().getCompany().getUser());



    }






}
