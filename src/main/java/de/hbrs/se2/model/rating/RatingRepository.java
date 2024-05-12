package de.hbrs.se2.model.rating;

import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.student.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findByStudent(Student student);

    @Query("SELECT r FROM Rating r WHERE r.company.id = :companyId")
    List<Rating> findRatingByCompany(@Param("companyId") UUID companyId);

    Rating findRatingByStudentAndCompany(Student student, Company company);
}
