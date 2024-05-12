package de.hbrs.se2.control.User;

import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.student.StudentService;
import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.student.Student;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    StudentService studentService;

    public boolean checkSecurityAnwser(String answer, String email) {
        return userRepository.getByEmail(email).getSecurityAnswer().equals(answer.trim().toLowerCase());
    }

    public BaseEntity identifyRole(User user) {
        Company company;
        try {
            company = companyService.getCompanyByEmail(user.getEmail().toLowerCase());
        } catch (NoSuchElementException e) {
            company = null;
        }

        Student student;
        try {
            student = studentService.getStudentByEmail(user.getEmail().toLowerCase());
        } catch (NoSuchElementException e) {
            student = null;
        }

        return student == null ? company : student;
    }
}
