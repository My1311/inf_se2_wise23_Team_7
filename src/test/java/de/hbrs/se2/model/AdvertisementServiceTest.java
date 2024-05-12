package de.hbrs.se2.model;

import de.hbrs.se2.control.advertisement.AdvertisementService;
import de.hbrs.se2.control.company.CompanyService;
import de.hbrs.se2.model.company.Company;
import de.hbrs.se2.model.company.CompanyRepository;
import de.hbrs.se2.model.jobadvertisement.Advertisement;
import de.hbrs.se2.model.jobadvertisement.AdvertisementDTO;
import de.hbrs.se2.model.jobadvertisement.AdvertisementRepository;
import de.hbrs.se2.model.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AdvertisementServiceTest {

    @InjectMocks
    private AdvertisementService advertisementService;

    @Mock
    private CompanyRepository companyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AdvertisementRepository advertisementRepository;

    @Mock
    private CompanyService companyService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAddAdvertisement() {
        // Mock für Company
        Company mockCompany = new Company("testCompany","software","description",null);
        when(companyService.getCompanyByEmail(anyString())).thenReturn(mockCompany);

        // Testfall: Advertisement hinzufügen
        assertThrows(IllegalArgumentException.class, () ->
                advertisementService.addAdvertisement("testCompanyEmail", "advertisement1", "Description", LocalDate.now(), 20.0, "Location", true, "Requirements")
        );

        // Verifiziere, dass die Repository-Methode mit den richtigen Parametern aufgerufen wurde
        verify(advertisementRepository, never()).save(any());
    }

    @Test
    public void testUpdateAdvertisementOfCompany() {
        // Mock für Advertisement
        Advertisement mockAdvertisement = new Advertisement("advertisement1", new Company("testCompany","software","description",null), true, "Description", LocalDate.now(), 20.0, "Location", "Requirements");
        when(advertisementRepository.getAdvertisementOfCompany(anyString(), anyString())).thenReturn(Collections.singletonList(mockAdvertisement));

        // Testfall: Advertisement aktualisieren
        advertisementService.updateAdvertisementOfCompany("testCompanyEmail", "advertisement1", "NewDescription", LocalDate.now(), 25.0, "NewLocation", false, "NewRequirements");

        // Verifiziere, dass die Repository-Methode mit den richtigen Parametern aufgerufen wurde
        verify(advertisementRepository, times(1)).save(any());
    }

    @Test
    public void testDeleteAdvertisement() {
        // Mock für Advertisement
        Advertisement mockAdvertisement = new Advertisement("advertisement1", new Company("testCompany","software","description",null), true, "Description", LocalDate.now(), 20.0, "Location", "Requirements");
        when(advertisementRepository.getAdvertisementOfCompany(anyString(), anyString())).thenReturn(Collections.singletonList(mockAdvertisement));

        // Testfall: Advertisement löschen
        advertisementService.deleteAdvertisement("testCompanyEmail", "advertisement1");

        // Verifiziere, dass die Repository-Methode mit den richtigen Parametern aufgerufen wurde
        verify(advertisementRepository, times(1)).deleteAdvertisementByCompany(any(), anyString());
    }

    // Weitere Tests für die anderen Methoden des AdvertisementService sind erforderlich

    @Test
    public void testDoPaging() {
        // Mock für Advertisement
        Advertisement mockAdvertisement1 = new Advertisement("advertisement1", new Company("testCompany1","software","description",null), true, "Description", LocalDate.now(), 20.0, "Location", "Requirements");
        Advertisement mockAdvertisement2 = new Advertisement("advertisement2", new Company("testCompany2","software","description",null), true, "Description", LocalDate.now(), 25.0, "Location", "Requirements");
        List<Advertisement> mockAdvertisements = Arrays.asList(mockAdvertisement1, mockAdvertisement2);
        when(advertisementRepository.findAll()).thenReturn(mockAdvertisements);

        // Testfall: Paging
        Iterator<List<AdvertisementDTO>> iterator = advertisementService.doPaging(1, null, null);

        // Erster Durchlauf
        List<AdvertisementDTO> page1 = iterator.next();
        assertEquals(1, page1.size());
        assertEquals("advertisement2", page1.get(0).getName());

        // Zweiter Durchlauf
        List<AdvertisementDTO> page2 = iterator.next();
        assertEquals(1, page2.size());
        assertEquals("advertisement1", page2.get(0).getName());

        // Kein weiterer Durchlauf
        boolean hasNext = iterator.hasNext();
        assertEquals(false, hasNext);

        // Verifiziere, dass die Repository-Methode mit den richtigen Parametern aufgerufen wurde
        verify(advertisementRepository, times(1)).findAll();
    }
}
