package de.hbrs.se2.model.company;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@EnableJpaRepositories
public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT c FROM Company c WHERE c.user.email = :email")
    Optional<Company> getByEmail(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Company c WHERE c.user.email = :email")
    void deleteByEmail(@Param("email") String email);

    @Query("SELECT c From Company c where c.name  = :name")
    List<Company> getByName(@Param("name") String name);

    @Query("select c from Company c where c.name like :name and c.industry like :industry")
    Page<Company> filteredCompanyByNameAndIndustry(@Param("name") String name, @Param("industry") String industry, Pageable page);

    @Query("select c from Company c where c.industry like :industry")
    Page<Company> filteredCompanyByIndustry(@Param("industry") String industry, Pageable page);

    @Query("select c from Company c where c.name like :name")
    Page<Company> filteredCompanyByName(@Param("name") String name, Pageable page);

    @Query("SELECT company FROM Company company WHERE company.user.id=:userId")
    @Nullable
    Company findCompanyByUser(@Param("userId") UUID userId);
}
