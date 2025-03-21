package com.ecommerce.automation.steps;

import com.ecommerce.automation.utils.Driver;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

@Log4j2
public class Hooks {

    @Before
    public void setup(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
    }

    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = Driver.getDriver();
        
        // Take screenshot if scenario fails
        if (scenario.isFailed()) {
            log.error("Scenario failed: {}", scenario.getName());
            
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot of failure");
                log.info("Screenshot taken for failed scenario: {}", scenario.getName());
            } catch (Exception e) {
                log.error("Failed to take screenshot: {}", e.getMessage());
            }
        }
        
        // Quit driver
        Driver.quitDriver();
        log.info("Scenario completed: {}, Status: {}", scenario.getName(), scenario.getStatus());
    }
} 