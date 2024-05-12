package de.hbrs.se2.model.advertisement;

import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.DefaultValues;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdvertisementServiceTest {

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private CompanyService companyService;

    @InjectMocks
    private AdvertisementService advertisementService;

    private Advertisement testAdvertisement;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mocking behavior for AdvertisementRepository
        when(advertisementRepository.getAdvertisementOfCompany(any(), any())).thenReturn(new ArrayList<>());

        // Mocking behavior for CompanyService
        when(companyService.getCompanyByEmail(anyString())).thenReturn(createTestCompany());

        // Create a test Advertisement
        testAdvertisement = createTestAdvertisement();

        // Create a test Company
        testCompany = createTestCompany();
    }

    @Test
    void addAdvertisement() {
        // Mocking behavior to simulate that the advertisement does not exist
        when(advertisementRepository.getAdvertisementOfCompany(any(), any())).thenReturn(new ArrayList<>());

        // Adding the advertisement
        advertisementService.addAdvertisement(testCompany.getUser().getEmail(), "TestAd", "Test Description",
                LocalDate.now(), 20.0, "TestLocation", true, "TestRequirements");

        // Verifying that save method is called with the correct Advertisement object
        verify(advertisementRepository, times(1)).save(any(Advertisement.class));
    }

    @Test
    void addAdvertisementWithExistingAdvertisement() {
        // Mocking behavior to simulate that the advertisement already exists
        when(advertisementRepository.getAdvertisementOfCompany(any(), any())).thenReturn(List.of(testAdvertisement));

        // Adding the advertisement
        assertThrows(IllegalArgumentException.class,
                () -> advertisementService.addAdvertisement(testCompany.getUser().getEmail(), "TestAd",
                        "Test Description", LocalDate.now(), 20.0, "TestLocation", true, "TestRequirements"));


        verify(advertisementRepository, never()).save(any(Advertisement.class));
    }


    private Advertisement createTestAdvertisement() {
        return new Advertisement("TestAd", testCompany, true, "Test Description",
                LocalDate.now(), 20.0, "TestLocation", "TestRequirements");
    }

    private Company createTestCompany() {
        return DefaultValues.DEFAULT_COMPANY;
    }
}

