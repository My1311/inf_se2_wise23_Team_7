package de.hbrs.se2.selenium;
import org.junit.Ignore;
import org.junit.jupiter.api.Test;

import org.openqa.selenium.By;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SeleniumCompRegi {
    @Ignore
    @Test
    public void CumpRegi(){
        ChromeOptions options = new ChromeOptions();
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("http://localhost:8080/register");

        WebElement email=driver.findElement(By.id("input-vaadin-email-field-37"));
        WebElement password1=driver.findElement(By.id("input-vaadin-password-field-38"));
        WebElement password2=driver.findElement(By.id("input-vaadin-password-field-39"));
        WebElement frage=driver.findElement(By.id("input-vaadin-combo-box-41"));
        WebElement frageGenau=driver.findElement(By.id("label-vaadin-email-field-26"));
        WebElement antwort=driver.findElement(By.id("input-vaadin-email-field-42"));
        WebElement comp=driver.findElement(By.id("input-vaadin-radio-button-28"));
        WebElement login=driver.findElement(By.id("login-button-3"));

        email.sendKeys("student@student.de");
        password1.sendKeys("test");
        password2.sendKeys("test");
        frage.click();
        frageGenau.click();
        antwort.sendKeys("Bonn");
        comp.click();
        login.click();
        String sollte = "http://localhost:8080/register/Company";
        String ist = driver.getCurrentUrl();
        assertEquals(sollte,ist);
        driver.quit();

    }
}
