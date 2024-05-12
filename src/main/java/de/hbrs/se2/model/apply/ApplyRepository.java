package de.hbrs.se2.model.apply;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

@EnableJpaRepositories
public interface ApplyRepository extends JpaRepository<Apply, UUID> {
    //  @Query("select advertisement from Advertisement advertisement "
    //                    + "where advertisement.company.id=:companyId ")
    //    List<Advertisement> findAdvertisementsByCompanyId(@Param("companyId") UUID companyId);

    // Befehle in DB: SELECT a FROM Apply a WHERE a.student = 'ee0babff-2f1b-4199-87d0-fd8b5242e904' AND a.advertisement = '651dc025-6f3a-43df-80ee-873525a3c5d4';

    @Query("SELECT a FROM Apply a WHERE a.student.id = :studentId AND a.advertisement.id = :advertisementId")
    Apply getApplyByStudentAndAdvertisement(@Param("studentId") UUID studentId, @Param("advertisementId") UUID advertisementId);


}
