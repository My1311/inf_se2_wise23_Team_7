package de.hbrs.se2.model.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface StudentRepository extends JpaRepository<Student, UUID> {

    Student findStudentByUserId(UUID userId);

    @Query("SELECT c FROM Student c WHERE c.user.email = :email")
    Optional<Student> getByEmail(@Param("email") String email);


    @Query("SELECT c FROM Student c  WHERE c.id IN (SELECT a.student.id FROM Apply a WHERE a.advertisement.id = :advertisementId)")
    List<Student> findAllofApplicantsforAdvert(@Param("advertisementId") UUID advertisementId);
}
