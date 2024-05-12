package de.hbrs.se2.model;

import de.hbrs.se2.model.apply.ApplicationState;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.student.StudentApplyDto;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class StudentApplyDTOTest {

    @Test
    public void testToDto() {
        // Erstellen Sie einen Student und einen ApplicationState
        Student student = new Student();
        ApplicationState applicationState = ApplicationState.PENDING;

        // Erstellen Sie ein StudentApplyDto mit dem Builder
        StudentApplyDto studentApplyDto = StudentApplyDto.toDto(student, applicationState);

        // Überprüfen Sie, ob die Werte korrekt gesetzt wurden
        assertThat(studentApplyDto.getStudent()).isEqualTo(student);
        assertThat(studentApplyDto.getApplicationState()).isEqualTo(applicationState);
    }

    @Test
    public void testBuilder() {
        // Erstellen Sie einen StudentApplyDto mit dem Builder
        StudentApplyDto studentApplyDto = StudentApplyDto.builder()
                .student(new Student())
                .applicationState(ApplicationState.ACCEPTED)
                .build();

        // Überprüfen Sie, ob die Werte korrekt gesetzt wurden
        assertThat(studentApplyDto.getStudent()).isNotNull();
        assertThat(studentApplyDto.getApplicationState()).isEqualTo(ApplicationState.ACCEPTED);
    }
}
