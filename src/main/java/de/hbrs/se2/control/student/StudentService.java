package de.hbrs.se2.control.student;

import de.hbrs.se2.control.apply.ApplyService;
import de.hbrs.se2.model.apply.Apply;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.student.StudentApplyDto;
import de.hbrs.se2.model.student.StudentRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ApplyService applyService;

//    public StudentDTO findStudentByUserID(UUID userId) {
////        return StudentDTO.toDto(this.studentRepository.findStudentByUserID(userId));
//    }

    public @Nullable Student addStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudentByUser(User user) {
        return this.studentRepository.findStudentByUserId(user.getId());
    }

    public List<Student> findAll() {
        return this.studentRepository.findAll();
    }

    public void delete(Student student) {
        this.studentRepository.delete(student);
        this.userRepository.delete(student.getUser());
    }


    public Student getStudentByEmail(String email) { // muss findCompanyByUserEmail aufrufen, um dies zu machen
        Optional<Student> studentOptional = studentRepository.getByEmail(email.toLowerCase());

        if (studentOptional.isPresent()) {
            return studentOptional.get();
        } else {
            // Throw an exception indicating that no company is found by the given email
            throw new NoSuchElementException("No student found for email: " + email);
        }
    }

    public List<StudentApplyDto> findAllofApplicantsforAdvert(Advertisement advertisement) {
        List<Student> student = this.studentRepository.findAllofApplicantsforAdvert(advertisement.getId());
        List<StudentApplyDto> result = new ArrayList<>();
        for (Student s : student) {
            Apply apply = this.applyService.getApplyByStudentAndAdvertisement(s, advertisement);
            result.add(StudentApplyDto.toDto(s, apply.getState()));
        }
        return result;
    }
}
