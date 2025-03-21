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
        "html:target/cucumber-reports/chrome/cucumber-pretty",
        "json:target/cucumber-reports/chrome/CucumberTestReport.json",
        "rerun:target/cucumber-reports/chrome/rerun.txt"
    },
    monochrome = true,
    publish = true,
    tags = "@e2e"
)
public class ChromeTestRunner {
    
    @BeforeClass
    public static void setup() {
        // Chrome için sistem özelliğini ayarla
        System.setProperty("browser.type", "chrome");
    }
} 