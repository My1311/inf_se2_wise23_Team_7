package de.hbrs.se2.model.jobadvertisement;

import de.hbrs.se2.model.company.CompanyDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class AdvertisementDTO {

    private String name;
    private String description;
    private LocalDate deadline;
    //private String jobType;

    private Double hourlyWage;

    private String ort;

    private boolean active;

    private String requirements;

    private CompanyDTO companyDTO;

    public static AdvertisementDTO toDTO(Advertisement ad) {
        return new AdvertisementDTO(ad.getName(), ad.getDescription(), ad.getDeadline(), ad.getHourlyWage(),
                ad.getOrt(), ad.isActive(), ad.getRequirements(), CompanyDTO.toDTO(ad.getCompany()));
        // return new AdvertisementDTO(ad.getName(), ad.getDescription(), ad.getDeadline(),ad.getJobType(), ad.getHourlyWage(),
        //                                       ad.getOrt(), ad.isActive(), ad.getRequirements(), CompanyDTO.toDTO(ad.getCompany()));
    }
}
