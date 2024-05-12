package de.hbrs.se2.model.rating;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.rating.RatingService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RatingRoundTripTest {
    @Autowired
    private LoginService loginService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private CompanyService companyService;
    private Apply apply;
    private Rating rating;

    @BeforeEach
    void setup() {
        rating = DefaultValues.DEFAULT_RATING;
    }

    @Test
    @Order(1)
    @DisplayName("Rating Value wird erstellt und in DB hinzugefugt.")
    void testInsertRatingValue() {
        this.loginService.addUser(this.rating.getStudent().getUser());
        this.loginService.addUser(this.rating.getCompany().getUser());
        studentService.addStudent(this.rating.getStudent());
        companyService.createCompany(this.rating.getCompany());
        ratingService.addRating(this.rating);


        Rating dbRating = this.ratingService.addRating(this.rating);
        assertNotNull(dbRating);
        assertEquals(this.rating.getId(), dbRating.getId());
    }

    @Order(2)
    @Test
    @DisplayName("Rating Value wird aus der DB gelesen.")
    void testReadRatingValue() {
        Rating dbRating = this.ratingService.findRatingByStudentAndCompany(this.rating.getStudent(), this.rating.getCompany());
        assertNotNull(dbRating);
        assertEquals(this.rating.getId(), dbRating.getId());
    }

    @Order(3)
    @Test
    @DisplayName("Rating Value wird nicht in der DB updated.")
    void testUpdateRatingValue() {
        this.rating.setValue(3);
        this.ratingService.addRating(this.rating);
        Rating dbRating = this.ratingService.findRatingByStudentAndCompany(this.rating.getStudent(), this.rating.getCompany());
        assertNotNull(dbRating);
        assertEquals(this.rating.getValue(), dbRating.getValue());
    }

    @Order(4)
    @Test
    @DisplayName("alle list of Rating Value von dieser Firma in der DB.")
    void testFindAllOfRatingInCompany() {
        List<Rating> listRatingDB = this.ratingService.findAllOfRatingInCompany(this.rating.getCompany());
        for (int i = 0; i < listRatingDB.size(); i++) {
            System.out.println(listRatingDB.get(i));
        }
        Rating dbRating = this.ratingService.findRatingByStudentAndCompany(this.rating.getStudent(), this.rating.getCompany());
        assertTrue(listRatingDB.contains(dbRating));
    }
    @Order(5)
    @Test
    @DisplayName("Durchschnittliche Rating Value wird gerechnet.")
    void testGetAverageRatingOfCompany() {
        // create another rating von student2 fur gleichen Company
        User user2 = User.builder()
                .email("testStudent2@gmail.com")
                .password("student2")
                .build();
        Student student2 = Student.builder()
                .user(user2)
                .first_name("Max")
                .last_name("Mustermann")
                .major_study("Test Student")
                .degree("Test Student")
                .list_of_skills(" Test Student")
                .build();
        Rating rating2 = Rating.builder()
                .company(DefaultValues.DEFAULT_COMPANY)
                .student(student2)
                .text("I like this company,too.")
                .value(5)
                .timestamp(Instant.now())
                .build();
        this.loginService.addUser(rating2.getStudent().getUser());
        studentService.addStudent(rating2.getStudent());
        ratingService.addRating(rating2);
        double averageRating = this.ratingService.getAverageRatingOfCompany(this.rating.getCompany());
        assertEquals(4, averageRating);

        ratingService.delete(rating2);
        studentService.delete(rating2.getStudent());
        this.loginService.delete(rating2.getStudent().getUser());

    }



    @Test
    @Order(6)
    @DisplayName("Rating Value wird aus der DB geloscht.")
    void testDeleteRatingValue() {
        this.ratingService.delete(rating);

        Rating dbRating = this.ratingService.findRatingByStudentAndCompany(this.rating.getStudent(), this.rating.getCompany());
        assertNull(dbRating);
        studentService.delete(this.rating.getStudent());
        companyService.deleteCompany(this.rating.getCompany());
        this.loginService.delete(this.rating.getStudent().getUser());
        this.loginService.delete(this.rating.getCompany().getUser());
    }
}
