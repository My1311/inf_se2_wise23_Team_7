package de.hbrs.se2.model.jobadvertisement;


import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.company.Company;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;

@SpringBootTest //um alle jpa beans fpr den test zu laden bsp. autowired

public class TestRepo {
    @Autowired
    //CompanyRepository companyRepository;
    AdvertisementRepository advertisementRepository;

    @Autowired
    CompanyService companyService;
    @Autowired
    AdvertisementService advertisementService;

    @Test
    public void saveMethod() {


        byte[] data = "Hello, this is dummy data.".getBytes();

        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(data);


//        String email = "WQwweWWAdwdwsdswwlfW";
//        String adName = "oemer3210";
//        companyService.createCompany(email, "password123", "Example Company",inputStream, "Tech", "A tech company");
//
//
//        //advertisementService.addAdvertisement(email,adName,"wir sind schlecht",LocalDate.of(2023, 12, 31), 3.0,"hausfrau", false, "d" );
//        advertisementService.addAdvertisement(email,adName,"wir sind schlecht", LocalDate.of(2023, 12, 31), 3.0,"hausfrau", false, "d" );
//        AdvertisementDTO a = advertisementService.getAdvertisementDTOOfCompanyByName(email, adName);


//        advertisementService.updateAdvertisementOfCompany(email, adName,"wir sind nicht schlecht",LocalDate.of(2023, 12, 31), 5.0,"hausfrau", false, "d"  );
//        a = advertisementService.getAdvertisementDTOOfCompanyByName(email, adName);


        String email = "AadfesAAadfswdfsrdf";
        String adName = "Teslee^rasdfasfkom";
        companyService.createCompany(email, "password123", "Example Company", inputStream, "Tech", "A tech company");
//
//
        advertisementService.addAdvertisement(email, adName, "wir sind schlecht", LocalDate.of(2023, 12, 31), 3.0, "hausfrau", false, "d");
//
//
//        advertisementService.addAdvertisement(email,adName,"wir sind schlecht",LocalDate.of(2023, 12, 31), 3.0,"hausfrau", false, "d" );
        AdvertisementDTO a = advertisementService.getAdvertisementDTOOfCompanyByName(email, adName);
//
//        System.out.println(a.getHourlyWage());
        advertisementService.updateAdvertisementOfCompany(email, adName, "wir sind schlecht", LocalDate.of(2023, 12, 31), 5.0, "hausfrau", false, "d");
        a = advertisementService.getAdvertisementDTOOfCompanyByName(email, adName);
        System.out.println(a.getHourlyWage());
        companyService.deleteCompany(companyService.getCompanyByEmail(email));

    }

    @Transactional
    @Test
    public void testPagind() {
        Advertisement ad;
        int j = 20;


        byte[] data = "Hello, this is dummy data.".getBytes();

        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(data);
        String[] companyName = new String[j];
        Company[] companyArray = new Company[j];

        String[] arrayCompany = new String[j];
        for(int i = 0; i < j; i++){
            arrayCompany[i] = i+" "+41141833;
            companyArray[i] = companyService.createCompany(arrayCompany[i], "password123", "" + i,inputStream, "Tech", "A tech company");

        }


        for (int i = 0; i < j; i++) {

            advertisementService.addAdvertisement(arrayCompany[i],i + "","wir sind schlecht",LocalDate.of(2023, 12, 31), 3.0,"hausfrau", false, "d" );
        }
        Iterator<List<AdvertisementDTO>> paging = advertisementService.doPaging(10, 1 + "", "Tech");

        while (paging.hasNext()) {
            for (AdvertisementDTO addd : paging.next()) {
                System.out.println(addd.getName());
            }
        }
        int i = 0;
        while (i < j) {
            companyService.deleteCompany(companyArray[i].getUser().getEmail());
            i++;

        }


    }


}
