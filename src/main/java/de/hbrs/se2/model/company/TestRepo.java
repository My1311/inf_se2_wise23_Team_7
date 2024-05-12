package de.hbrs.se2.model.company;

import de.hbrs.se2.control.UnfinshedUserService.UnfinishedUserService;
import de.hbrs.se2.control.User.UserService;
import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.UndefinedUser.UnfinishedUserRepository;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;


@SpringBootTest //um alle jpa beans fpr den test zu laden bsp. autowired

public class TestRepo {
    @Autowired
    //CompanyRepository companyRepository;
    CompanyRepository companyRepository;
    @Autowired
    CompanyService companyService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    AdvertisementService advertisementService;
    @Autowired
    UserService userService;
    @Autowired
    UnfinishedUserRepository unfinishedUserRepository;
    @Autowired
    UnfinishedUserService unfinishedUserService;


    //@Test

    void saveMethod() {
    /*        User user = new User("example@example.com", "password123");
        userRepository.save(user);
        // Create a Company and associate it with the User
        Company c = new Company("Example Company",null, "Tech", "A tech company", "+123456789", user);
        companyRepository.save(c);*/

        byte[] data = "Hello, this is dummy data.".getBytes();

        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(data);
        String email = "ex12ampsdeelq<ay<sqqwe@aa112@examoplewweww.com";
        companyService.createCompany(email, "password123", "Example Company", inputStream, "Tech", "A tech company");
        Company comp = companyService.getCompanyByEmail(email);
        CompanyDTO compDTO = companyService.getCompanyDTOByEmail(email);
//        advertisementRepository.save(new Advertisement("Telekqwom",xdyxy<cy<XS<", LocalDate.of(2023, 12, 31), 23.0, "Bonn", true, "h", comp));
        System.out.println("____");
        System.out.println("        ");
        System.out.println(comp.getId() + " steht in beziehung mit" + comp.getId());
        System.out.println("        ");
        System.out.println("____");
        System.out.println("CompanyDTO email:" + compDTO.getEmail());
        //companyService.deleteCompany(email);
        for (AdvertisementDTO ad : advertisementService.getAllAddsOfCompany(email)) {
            System.out.println(ad.getName());
        }


    }

    //@Test
    void readMethod() {
        String email = "exampwlqiuiqe@äexamplasase.com";
        User user = new User(email, "password123");
        userRepository.save(user);
        // Create a Company and associate it with the User
        Company c = new Company("Example Company", null, "Tech", "A tech company", user);
        companyRepository.save(c);
        System.out.println("____");
        System.out.println("        ");
        System.out.println(c.getId() + " steht in beziehung mit" + user.getId());
        System.out.println("        ");
        System.out.println("____");

        User u2 = companyService.getCompanyByEmail(email).getUser();
        System.out.println(u2.getId());


    }

    @Test
    void readByEmail() {
        String s = "exajmqömple@exammklkple1.comwsd";
        User user = new User(s, "password123");
        userRepository.save(user);
        // Create a Company and associate it with the User
        Company c = new Company("Example Company", null, "Tech", "A tech company", user);
        companyRepository.save(c);
        CompanyDTO c2 = companyService.getCompanyDTOByEmail(s); // muss jedes ml eine neue email nehem, da sonst reuslt mengenwertig ist
        System.out.println(c2.getEmail());
    }


    /**
     * thiss shows that the advertisement cahnged wothout hacong the company but retrieveing the correspoding company afterward, will have the changes. so some consistency is given( it is trivial because of the nature of relational db)
     */
    //@Test
    void editAdvertisement() {
        //String email = " hshklkjkjkdd";
        //companyService.createCompany(email, "password123", "Example Company",null, "Tech", "A tech company", "+123456789");
        //Company company = companyService.getCompanyByEmail(email);
        //Advertisement ad = new Advertisement("Telekom","wir sind schlecht", LocalDate.of(2023, 12, 31), 23.0, "Bonn", true, "h");
        //advertisementRepository.save(ad);
        //company.addAdvertisement(ad);
        //companyRepository.save(company);
        //ad.setDescription("sdfsl ");
        //advertisementRepository.save(ad);
        //CompanyDTO companyDTO = companyService.getCompanyDTOByEmail(email);


        //System.out.println(companyDTO.getAdvertisements().get(0).getDescription());


    }

    @Transactional // so i can do delete and isert in the same method
    @Test
    public void pagingTest() {
        int length = 1;
        byte[] data = "Hello, this is dummy data.".getBytes();

        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(data);
        String[] array = new String[length];
        for (int i = 0; i < length; i++) {
            array[i] = i + "";
            companyService.createCompany(array[i], "password123", i + "", inputStream, i + "", "A tech company");

        }
        Iterator<List<CompanyDTO>> d = companyService.doPaging(10, null, "gaming");

        int i = 0;
        while (d.hasNext()) {
            for (CompanyDTO dto : d.next()) {
                System.out.println(dto.getName() + " " + dto.getIndustry());

            }
            System.out.println("page: " + i++);
        }
        i = 0;

        while (i < length) {
            companyService.deleteCompany(array[i++]);
            //i++;
        }


    }

    //@Transactional
    //
    // @Test
    public void deleteTeste() {
        byte[] data = "Hello, this is dummy data.".getBytes();
        String email = "sdf";
        // Create a ByteArrayInputStream from the byte array
        InputStream inputStream = new ByteArrayInputStream(data);
        companyService.createCompany(email, "password123", "Example Company", inputStream, "Tech", "A tech company");
        Company company = companyService.getCompanyByEmail(email);
        Advertisement a = new Advertisement();
        companyService.deleteCompany(email);


    }

    //@Test
    public void testPassword() {
        String email = "asqdsf";
        User user = new User(email, "asdf", "hello", "asd ");
        userRepository.save(user);
        System.out.println(userService.checkSecurityAnwser(" asd ", email));

//        UnfinishedUser unfinishedUser = new UnfinishedUser(user, "company");
//        unfinishedUserRepository.save(unfinishedUser);
//        unfinishedUserRepository.delete(unfinishedUser);
        boolean b = unfinishedUserService.checkIfUserIsUnfinishedUser(user.getEmail());

        System.out.println(b);

        System.out.println(user.getEmail());
        userRepository.delete(user);
    }
}
