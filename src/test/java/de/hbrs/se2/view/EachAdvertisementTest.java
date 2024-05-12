package de.hbrs.se2.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import static org.junit.Assert.assertTrue;

public class EachAdvertisementTest {

    private WebDriver driver;

    @Before
    public void setUp() {
        System.setProperty("webdriver.chrome.driver", "/Pfad/zum/chromedriver");
        driver = new ChromeDriver();
        driver.get("http://localhost:8080/your-application-url");
    }

    @Ignore
    @Test
    public void testApplyButtonClicked() {
        // Aktionen mit Selenium: Klicken Sie auf den Bewerbungsbutton
        WebElement applyButton = driver.findElement(By.id("applyButton")); // Annahme, dass das Element eine ID hat
        applyButton.click();

        // Überprüfen, ob die erwartete Benutzeroberflächenänderung erfolgt ist
        WebElement confirmation = driver.findElement(By.id("confirmation"));
        assertTrue(confirmation.getText().contains("Your application letter is now sent."));
    }

    // Weitere Tests können hinzugefügt werden, um verschiedene Interaktionen und Überprüfungen durchzuführen

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
