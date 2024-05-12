package de.hbrs.se2.control.advertisement;

import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.control.page.Paging;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyDTO;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import de.hbrs.se2.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class AdvertisementService {
    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CompanyService companyService;

    private List<AdvertisementDTO> convertToDTO(List<Advertisement> advertisementList) {
        List<AdvertisementDTO> result = new ArrayList<>();
        for (Advertisement add : advertisementList) {
            result.add(AdvertisementDTO.toDTO(add));
        }
        return result;
    }


    @Transactional
    public void addAdvertisement(String comapanyEmail, String advertisementName, String description, LocalDate deadline, Double hourlywage,
                                 String ort, boolean active, String requirements) throws IllegalArgumentException {
        List<Advertisement> companies = advertisementRepository.getAdvertisementOfCompany(comapanyEmail, advertisementName);

        if (!companies.isEmpty()) {
            CompanyDTO company = companyService.getCompanyDTOByEmail(comapanyEmail);
            throw new IllegalArgumentException("Advertisement " + advertisementName + "already exists for " + company.getName());
        }
        Company company = companyService.getCompanyByEmail(comapanyEmail);
        Advertisement advertisement = new Advertisement(advertisementName, company, active, description, deadline, hourlywage, ort, requirements);
        advertisementRepository.save(advertisement);

    }

    @Transactional
    public void updateAdvertisementOfCompany(String comapanyEmail, String advertisementName, String description, LocalDate deadline, Double hourlywage,
                                             String ort, boolean active, String requirements) throws NoSuchElementException {

        Advertisement ad = getAdvertisementOfCompanyByName(comapanyEmail, advertisementName);
        ad.setDescription(description);
        ad.setDeadline(deadline);
        ad.setHourlyWage(hourlywage);
        ad.setOrt(ort);
        ad.setActive(active);
        ad.setRequirements(requirements);
        advertisementRepository.save(ad);
    }


    /**
     * ensures that even if companiers have an advertisement with the same name, that only the advertisemetn of the company gets deleted
     *
     * @param
     * @param advertisementName
     */
    @Transactional// because it is more than one transaction
    public void deleteAdvertisement(String companyEmail, String advertisementName) {

        advertisementRepository.deleteAdvertisementByCompany(companyService.getCompanyByEmail(companyEmail).getId(), advertisementName);
    }


    public List<AdvertisementDTO> getAllAddsOfCompany(String email) {

        return convertToDTO(advertisementRepository.getAllOfCompany(email));
    }


    private Advertisement getAdvertisementOfCompanyByName(String companyEmail, String advertisementName) throws NoSuchElementException {
        List<Advertisement> advertisement = advertisementRepository.getAdvertisementOfCompany(companyEmail.toLowerCase(), advertisementName);
        if (!advertisement.isEmpty()) {
            return advertisement.get(0);
        }
        CompanyDTO companyDTO = companyService.getCompanyDTOByEmail(companyEmail);
        throw new NoSuchElementException("Advertisement ''" + advertisementName + "'' does not exist for Company " + companyDTO.getName());
    }

    public AdvertisementDTO getAdvertisementDTOOfCompanyByName(String companyEmail, String advertisementName) throws NoSuchElementException {
        return AdvertisementDTO.toDTO(getAdvertisementOfCompanyByName(companyEmail, advertisementName));
    }

    @Transactional
    public List<Advertisement> getAllAdvertisementOfCompany(Company company) {
        return advertisementRepository.getAllOfCompany(company.getUser().getEmail());
    }

    @Transactional
    public List<Advertisement> findAll() {
        return advertisementRepository.findAll();
    }

    /**
     * inserts a new/ updates advertisement into the database
     *
     * @param advertisement the advertisement to be inserted
     */
    @Transactional
    public Advertisement insert(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }


    @Transactional
    public Advertisement findAdvertisementById(UUID id) {
        return advertisementRepository.findAdvertisementById(id);
    }

    @Transactional
    public void delete(Advertisement advertisement) {
        advertisementRepository.delete(advertisement);
    }

    public Iterator<List<AdvertisementDTO>> doPaging(int pageSize, String companyName, String companyIndustry) {
        return new Iterator<List<AdvertisementDTO>>() {

            final Iterator<List<Advertisement>> result;


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
                result= new Paging().doPaging(pageSize, "advertisementRepository",
                        new Sort.Order[]{new Sort.Order(Sort.Direction.DESC, "name")}, //filter first for comapny name
                        methodName, s);

            }



            @Override
            public boolean hasNext() {
                return result.hasNext();
            }

            @Override
            public List<AdvertisementDTO> next() {
                return convertToDTO(result.next());
            }
        };

    }
}
