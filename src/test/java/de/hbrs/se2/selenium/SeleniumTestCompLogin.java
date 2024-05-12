//package de.hbrs.se2.selenium;
//
//import org.junit.jupiter.api.Test;
//import io.github.bonigarcia.wdm.WebDriverManager;
//import org.junit.jupiter.api.Test;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Keys;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
//import java.time.LocalDate;
//import java.time.Month;
//import java.time.format.DateTimeFormatter;
//
//import static java.time.Duration.ofSeconds;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.openqa.selenium.support.ui.ExpectedConditions.titleIs;
//import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
//
//public class SeleniumTestCompLogin {
//    @Ignore
//    @Test
//    public void CompLogin(){
//        ChromeOptions options = new ChromeOptions();
//        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
//        options.addArguments("--remote-allow-origins=*");
//        WebDriver driver = new ChromeDriver(options);
//        driver.manage().window().maximize();
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
//
//        driver.get("http://localhost:8080/login");
//
//        WebElement username=driver.findElement(By.id("input-vaadin-email-field-236"));
//        WebElement password=driver.findElement(By.id("input-vaadin-password-field-237"));
//        WebElement login=driver.findElement(By.name("login-button-2"));
//
//
//        username.sendKeys("test@test.de");
//        password.sendKeys("tester");
//        login.click();
//
//
//
//        String actualUrl="editCompanyProfile";
//        String expectedUrl= driver.getCurrentUrl();
//        assertEquals(expectedUrl,actualUrl);
//        driver.quit();
//    }
//}
