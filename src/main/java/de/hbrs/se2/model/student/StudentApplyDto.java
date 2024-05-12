package de.hbrs.se2.model.student;

import de.hbrs.se2.model.apply.ApplicationState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class StudentApplyDto {
    private Student student;
    private ApplicationState applicationState;

    public static StudentApplyDto toDto(Student student, ApplicationState applicationState) {
        StudentApplyDto retStudent = StudentApplyDto.builder()
                .student(student)
                .applicationState(applicationState)
                .build();
        return retStudent;
    }
}
