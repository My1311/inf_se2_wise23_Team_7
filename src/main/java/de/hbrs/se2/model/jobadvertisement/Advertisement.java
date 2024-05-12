package de.hbrs.se2.model.jobadvertisement;

import de.hbrs.se2.model.common.BaseEntity;
import de.hbrs.se2.model.company.Company;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Advertisment", schema = "public")
public class Advertisement extends BaseEntity {

    @Builder.Default
    @Column(name = "name", nullable = false)
    private String name = null;

    @Builder.Default
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(
            //name = "company_id",
            name = "fk_company",
            referencedColumnName = "id",
            //foreignKey = @ForeignKey(name = "fk_company_id")
            foreignKey = @ForeignKey(name = "fk_from_advertisement_to_company")
    )
    private Company company = null;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean isActive = true;

    @Builder.Default
    @Column(name = "description", nullable = false, length = 10000)
    private String description = "";


    @Builder.Default
    @Column(name = "deadline", nullable = false, columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDate deadline = LocalDate.now();

    @Builder.Default
    @Column(name = "hourlywage", nullable = false)
    private Double hourlyWage = 0.0;

//    @Builder.Default
//    @Column(name = "jobType", nullable = false)
//    private String jobType = null;

    @Builder.Default
    @Column(name = "ort", nullable = false)
    private String ort = "";

    @Builder.Default
    @Column(name = "requirements", nullable = false)
    private String requirements = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Advertisement that = (Advertisement) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getCompany() != null ? !getCompany().equals(that.getCompany()) : that.getCompany() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getDeadline() != null ? !getDeadline().equals(that.getDeadline()) : that.getDeadline() != null)
            return false;
        //    if (getJobType() != null ? !getJobType().equals(that.getJobType()) : that.getJobType() != null) return false;
        if (getOrt() != null ? !getOrt().equals(that.getOrt()) : that.getOrt() != null) return false;
        if (getRequirements() != null ? !getRequirements().equals(that.getRequirements()) : that.getRequirements() != null)
            return false;
        return getHourlyWage() != null ? getHourlyWage().equals(that.getHourlyWage()) : that.getHourlyWage() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getCompany() != null ? getCompany().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getDeadline() != null ? getDeadline().hashCode() : 0);
        //    result = 31 * result + (getJobType() != null ? getJobType().hashCode() : 0);
        result = 31 * result + (getOrt() != null ? getOrt().hashCode() : 0);
        result = 31 * result + (getRequirements() != null ? getRequirements().hashCode() : 0);
        result = 31 * result + (getHourlyWage() != null ? getHourlyWage().hashCode() : 0);
        return result;
    }
}
