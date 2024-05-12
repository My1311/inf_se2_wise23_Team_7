package de.hbrs.se2.model.jobadvertisement;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

@EnableJpaRepositories
public interface AdvertisementRepository extends JpaRepository<Advertisement, UUID> {
    @Query("select advertisement from Advertisement advertisement "
            + "where advertisement.company.id=:companyId ")
    List<Advertisement> findAdvertisementsByCompanyId(@Param("companyId") UUID companyId);

    @Query("select advertisement from Advertisement advertisement "
            + "where advertisement.id=:advertisementId")
    Advertisement findAdvertisementById(@Param("advertisementId") UUID advertisementId);

    @Query("SELECT a FROM  Advertisement a WHERE a.name like :advertisementName AND a.company.user.email = :email")
    List<Advertisement> getAdvertisementOfCompany(@Param("email") String email, @Param("advertisementName") String advertisementName);

    @Modifying
    @Query("DELETE FROM Advertisement a WHERE a IN :advertisements")
    void deleteAllFormList(@Param("advertisements") List<Advertisement> advertisements);

    /**
     * deletes all advertisements once issued by a company
     *
     * @param
     */
    @Modifying
    @Query("DELETE FROM Advertisement a WHERE a.company IN (SELECT c FROM Company c WHERE c.id = :compynId )")
    void deleteAllAdvertisementByCompany(@Param("compynId") UUID compynId);

    @Modifying
    @Query("DELETE FROM Advertisement a WHERE a.company IN (SELECT c FROM Company c WHERE c.id = :companyId  AND a.name = :name)")
    void deleteAdvertisementByCompany(@Param("companyId") UUID companyId, @Param("name") String name);


    @Query("SELECT a FROM Advertisement a")
    List<Advertisement> findAllAdvertisement();


    @Query("SELECT a FROM Advertisement a JOIN a.company c WHERE c.user.email = :email")
    List<Advertisement> getAllOfCompany(@Param("email") String email);


    @Query("select a from Advertisement a where a.company.name like :name and a.company.industry like :industry")
    Page<Advertisement> filteredCompanyByNameAndIndustry (@Param("name") String name, @Param("industry") String industry, Pageable page);
    @Query("select a from Advertisement a where a.company.industry like :industry")
    Page<Advertisement> filteredCompanyByIndustry ( @Param("industry") String industry, Pageable page);
    @Query("select a from Advertisement a where a.company.name like :name")
    Page<Advertisement> filteredCompanyByName(@Param("name") String name, Pageable page);


}

