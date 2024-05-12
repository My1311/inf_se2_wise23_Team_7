package de.hbrs.se2.model.company;


import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class CompanyDTO {
    final private String name;
    final private byte[] logo; //
    final private String industry;
    final private String description;
    final private String phoneNumber;
    final private String email;
    final private String hashedPassword;
    final private List<Advertisement> advertisementList;


    public static CompanyDTO toDTO(Company c) {
        User user = c.getUser();

        return new CompanyDTO(c.getName(), c.getLogo(), c.getIndustry(), c.getDescription(), c.getPhoneNumber(), user.getEmail(), user.getPassword(), c.getAdvertisements());
    }


    //riehenfolge wird erhalne in oneToMany, daherak nn ich einfacher löschen und nee advertisements hinzufügen


}

