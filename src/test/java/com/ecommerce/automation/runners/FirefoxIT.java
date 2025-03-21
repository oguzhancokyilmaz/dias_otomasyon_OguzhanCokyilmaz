package com.ecommerce.automation.runners;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.ecommerce.automation.steps", "com.ecommerce.automation.stepdefinitions"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/firefox/cucumber-pretty",
        "json:target/cucumber-reports/firefox/CucumberTestReport.json",
        "rerun:target/cucumber-reports/firefox/rerun.txt"
    },
    monochrome = true,
    publish = true,
    tags = "@e2e"
)
public class FirefoxIT {
    
    @BeforeClass
    public static void setup() {
        // Firefox için sistem özelliğini ayarla
        System.setProperty("browser.type", "firefox");
    }
} 