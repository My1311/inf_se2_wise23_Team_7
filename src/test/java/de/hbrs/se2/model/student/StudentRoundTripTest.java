package de.hbrs.se2.model.student;

import de.hbrs.se2.Application;
import de.hbrs.se2.control.LoginService;
import de.hbrs.se2.control.student.StudentService;
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
class StudentRoundTripTest {

    @Autowired
    private LoginService userService;
    @Autowired
    private StudentService studentService;
    private Student student;

    @BeforeEach
    void setup() {
        student = DefaultValues.DEFAULT_STUDENT;
    }

    @Test
    @Order(1)
    @DisplayName("Student wird erstellt und in DB hinzugefugt.")
    void testInsertStudent() {
        User dbUser = this.userService.addUser(this.student.getUser());
        assertEquals(this.student.getUser(), dbUser);
        Student dbStudent = this.studentService.addStudent(this.student);
        // Die Methode findStudentByUser funktioniert gut!
        assertNotNull(student);
        assertEquals(student, dbStudent);
        assertEquals(student.getFirst_name(), dbStudent.getFirst_name());
        assertEquals(student.getLast_name(), dbStudent.getLast_name());
        assertEquals(student.getMajor_study(), dbStudent.getMajor_study());
        assertEquals(student.getDegree(), dbStudent.getDegree());
        assertEquals(student.getList_of_skills(), dbStudent.getList_of_skills());
    }

    @Order(2)
    @Test
    @DisplayName("Student wird aus der DB gelesen.")
    void testReadStudent() {
        Student dbStudent = this.studentService.findStudentByUser(this.student.getUser());
        assertNotNull(dbStudent);
        assertEquals(this.student, dbStudent);
    }


    @Order(3)
    @Test
    @DisplayName("Student wird in der DB updated.")
    void testUpdateStudent() {
        this.student.setMajor_study("TestUpdateTestUpdate");
        this.studentService.addStudent(this.student);
        Student dbStudent = this.studentService.findStudentByUser(this.student.getUser());
        assertNotNull(dbStudent);
        assertEquals(this.student.getMajor_study(), dbStudent.getMajor_study());
    }

    @Test
    @Order(4)
    @DisplayName("Student wird aus der DB geloscht.")
    void testDeleteStudent() {
        this.studentService.delete(student);
        Student dbStudent = this.studentService.findStudentByUser(this.student.getUser());
        assertNull(dbStudent);
        this.userService.delete(student.getUser());
        User dbUser = this.userService.findUserByEmail(this.student.getUser().getEmail());
        assertNull(dbUser);
    }

    @Test
    @Order(5)
    @DisplayName("Student wird durch gegebene Email gefunden können oder nicht.")
    void testGetStudentByEmail() {
        User dbUser = this.userService.addUser(this.student.getUser());
        Student dbStudent = this.studentService.addStudent(this.student);
        assert dbUser != null;
        Student foundStudent = this.studentService.getStudentByEmail(dbUser.getEmail());
        assertNotNull(foundStudent);
        assertEquals(foundStudent, dbStudent);
        this.studentService.delete(student);
        this.userService.delete(student.getUser());
    }
}
