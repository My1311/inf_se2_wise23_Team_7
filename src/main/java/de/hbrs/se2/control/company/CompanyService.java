package de.hbrs.se2.control.company;


import com.vaadin.flow.component.html.Image;
import de.hbrs.se2.control.Image.ImageService;
import de.hbrs.se2.control.page.Paging;
import de.hbrs.se2.control.rating.RatingService;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyDTO;
import de.hbrs.se2.model.company.CompanyRepository;

import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import de.hbrs.se2.model.user.User;
import de.hbrs.se2.model.user.UserRepository;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.InputStream;
import java.util.*;

@Service
public class CompanyService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private RatingService ratingService;



    /*public @Nullable Student addStudent(Student student){
        return studentRepository.save(student);
    }*/

    public @Nullable Company addCompany(Company company) { // code von my aus StudentService in angepasster form
        return companyRepository.save(company);
    }

    private List<CompanyDTO> convertToDTO(Iterable<Company> e) {
        List<CompanyDTO> companyDTOS = new ArrayList<>();
        for (Company company : e) {
            companyDTOS.add(CompanyDTO.toDTO(company));
        }
        return companyDTOS;
    }

    /**
     * @return a list of the representetive dto objects of the companies
     */
    public List<CompanyDTO> readAllCompanyDTO() {
        return convertToDTO(companyRepository.findAll());// findAll is given by jpaReopsitory and returns the dto
    }

    private List<Company> getCompanyByName(String name) {
        return companyRepository.getByName(name);
    }

    public List<CompanyDTO> getCompoanyDTOByName(String name) {
        return convertToDTO(getCompanyByName(name));

    }

    public Company getCompanyByEmail(String email) { // muss findCompanyByUserEmail aufrufen, um dies zu machen
        Optional<Company> companyOptional = companyRepository.getByEmail(email.toLowerCase());

        if (companyOptional.isPresent()) {
            return companyOptional.get();
        } else {
            // Throw an exception indicating that no company is found by the given email
            throw new NoSuchElementException("No company found for email: " + email);
        }
    }

    public CompanyDTO getCompanyDTOByEmail(String email) {
        return CompanyDTO.toDTO(getCompanyByEmail(email));
    }

    public void createCompany(String email, String password, String name, String industry, String description) {
        User user = new User(email, password);
        userRepository.save(user);
        Company company = new Company(name, industry, description, user);
        companyRepository.save(company);
    }

    @Transactional
    public Company createCompany(String email, String password, String name, InputStream logo, String industry, String description) {
        User user = new User(email, password);
        userRepository.save(user);
        return companyRepository.save(new Company(name, imageService.inputStreamToByte(logo), industry, description, user));
    }

    /**
     * hibernate has a bug in its old version, so it canntot cascade delete. therefor a work around
     *

     *
     *
     * @param companyEmail
     */

    @Transactional
    public void deleteCompany(String companyEmail) {
        Company company = getCompanyByEmail(companyEmail);
        companyRepository.delete(company);
    }

    /**
     *
     * @param pageSize
     * @param companyName
     * @param companyIndustry
     *
     * companyName == null and companyIndustry == null: does no filtering, returns all
     * companyIndustry == null and companyName != null: only filters by company name
     * @return
     */

    public Iterator<List<CompanyDTO>> doPaging(int pageSize, String companyName, String companyIndustry) {
        return new Iterator<List<CompanyDTO>>() {

            final Iterator<List<Company>> result;


            {
                String[] s;
                String methodName;
                if (companyName == null && companyIndustry == null){
                    s = null;
                    methodName = "findAll";
                }else if(companyIndustry != null && companyName == null){
                    s = new String[]{companyIndustry};
                    methodName = "filteredCompanyByIndustry";
                }else if(companyIndustry == null && companyName != null){
                    s = new String[]{companyName};
                    methodName = "filteredCompanyByName";
                    //if both valus are given, filter with both parameters
                }else{
                    s = new String[]{companyName, companyIndustry};
                    methodName = "filteredCompanyByNameAndIndustry";
                }
                    result= new Paging().doPaging(pageSize, "companyRepository",
                                                  new Sort.Order[]{new Sort.Order(Sort.Direction.ASC, "name"), //filter first for comapny name
                                                                   new Sort.Order(Sort.Direction.DESC, "industry")}, // than filer for Company branch
                                                  methodName, s);

                }

            @Override
            public boolean hasNext() {
                return result.hasNext();
            }

            @Override
            public List<CompanyDTO> next() {
                return convertToDTO(result.next());
            }
        };

    }

    @Transactional
    public void setLogo(String companyEmail, InputStream inputStream) {
        Company company = getCompanyByEmail(companyEmail);
        company.setLogo(imageService.inputStreamToByte(inputStream));
    }


    public Image getImage(byte[] logo) {
        return imageService.byteToImage(logo);
    }


    public Company createCompany(Company company) {
        return this.companyRepository.save(company);
    }

    public void deleteCompany(Company company) {
        this.companyRepository.delete(company);
    }

    public double getRatingPunkt(Company company) {
        return ratingService.getAverageRatingOfCompany(company);
    }



}
